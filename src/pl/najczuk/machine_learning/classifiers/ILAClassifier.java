package pl.najczuk.machine_learning.classifiers;

import pl.najczuk.machine_learning.instances.Attribute;
import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;
import java.util.Random;

/**
 * User: Adrian
 * Date: 11/16/2014
 * Time: 16:17
 */
public class ILAClassifier extends Classifier{
    private Integer[] optimalCombination;

    public ILAClassifier(Instances trainingInstances) {
        super(trainingInstances);
    }

    public Double classify(Instance instance) {
        for (Rule rule : getRules()) {
            if (matchesRule(instance, rule)) {
                return rule.getClassAttributeValue();
            }

        }
        //if no classification return random class
        int numberOfClasses = getAttributes().get(getAttributes().size() - 1).getNominalValuesMap().size();
        Random rand = new Random();
        Integer randomNum = rand.nextInt(numberOfClasses);
        return randomNum.doubleValue();
    }

    private boolean matchesRule(Instance instance, Rule rule) {
        Integer[] ruleAttributesI = rule.getAttributesIndexes();
        Double[] attributeValues = rule.getValues();
        boolean matches = true;
        for (int attributeI = 0; attributeI < ruleAttributesI.length; attributeI++) {
            int attributeToCheck = ruleAttributesI[attributeI];
            Double ruleAttributeValue = attributeValues[attributeI];
            Double instanceAttributeValue = instance.getValues().get(attributeToCheck);
            if (!ruleAttributeValue.equals(instanceAttributeValue)) {
                matches = false;
                break;
            }
        }

        return matches;
    }


    public ArrayList<Rule> trainClassifier() {
        Instances trainingInstances = getTrainingInstances();
        ArrayList<Attribute> attributes = trainingInstances.getAttributes();
        ArrayList<ArrayList<Integer[]>> classPartitions = getIntegerPartitions(trainingInstances);
        printPartitions(classPartitions);
        ArrayList<Rule> rules = getRules(classPartitions, attributes.size() - 1);

        return rules;
    }



    private Integer[] doubleValuesToIntegerArray(int attributesCount, ArrayList<Double> currentInstanceValues) {
        Integer[] integerArray = new Integer[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            integerArray[i] = currentInstanceValues.get(i).intValue();
        }
        return integerArray;
    }

    private int getIndexOfNewSubArray(ArrayList<ArrayList<Integer[]>> classSubArrays) {
        return classSubArrays.size() - 1;
    }


    private ArrayList<Rule> getRules(ArrayList<ArrayList<Integer[]>> partitions, int noOfAttributes) {
        ArrayList<Rule> rules = new ArrayList<>();
        for (int partitionI = 0; partitionI < partitions.size(); partitionI++) {
            int attrSpan = 1;
            ArrayList<Integer[]> combinations;
            boolean[] classifiedInstances = new boolean[partitions.get(partitionI).size()];

            while (!allInstancesClassified(classifiedInstances) && attrSpan <= noOfAttributes) {
                Integer[] maxCombination = new Integer[attrSpan];
                combinations = generateCombinations(noOfAttributes, attrSpan);
                ArrayList<Integer> maxCombinationInstances = getMaxCombinationForCombinations(combinations,
                        maxCombination, partitions, partitionI, classifiedInstances);
                for (Integer maxInstance : maxCombinationInstances) {
                    classifiedInstances[maxInstance] = true;

                }
                if (!maxCombinationInstances.isEmpty()) {
                    rules.add(generateRuleFromMaxCombination(partitions, partitionI, optimalCombination, maxCombinationInstances));
                }
                if (maxCombinationInstances.isEmpty()) {
                    attrSpan++;
                }
            }

        }

        return rules;
    }

    private Rule generateRuleFromMaxCombination(ArrayList<ArrayList<Integer[]>> partitions, int partitionI, Integer[]
            maxCombination, ArrayList<Integer> maxCombinationInstances) {
        Integer representativeInstanceIndex = maxCombinationInstances.get(0);
        Integer[] representativeInstance = partitions.get(partitionI).get(representativeInstanceIndex);
        Integer[] ruleValues = new Integer[maxCombination.length];
        Double instanceClassValue = representativeInstance[representativeInstance.length - 1].doubleValue();
        for (int ruleValueI = 0; ruleValueI < maxCombination.length; ruleValueI++) {
            ruleValues[ruleValueI] = representativeInstance[maxCombination[ruleValueI]];
        }
        Rule rule = new Rule(getAttributes(), maxCombination, ruleValues, instanceClassValue);
        return rule;
    }

    private boolean allInstancesClassified(boolean[] classifiedInstances) {
        for (int instanceI = 0; instanceI < classifiedInstances.length; instanceI++) {
            if (!classifiedInstances[instanceI])
                return false;
        }
        return true;
    }


    private ArrayList<Integer> getMaxCombinationForCombinations(ArrayList<Integer[]> combinations,
                                                                Integer[] maxCombination,
                                                                ArrayList<ArrayList<Integer[]>> partitions,
                                                                int mainPartitionI,
                                                                boolean[] classifiedInstances) {

        int globalMaxCombinationIndex = -1;
        int globalMaxCombinationInstancesCount = 0;
        ArrayList<Integer> globalMaxCombinationInstances = new ArrayList<>();

        int combinationIndex = 0;
        for (Integer[] combination : combinations) {
            ArrayList<ArrayList<Integer>> combinationValuesGroups = getCombinationValuesGroups(combination,
                    partitions.get(mainPartitionI), classifiedInstances);
            ArrayList<ArrayList<Integer>> deduplicatedCombinations = removeDuplicatesFromCombinationValuesGroups
                    (combination, combinationValuesGroups, mainPartitionI, partitions);
            ArrayList<Integer> maxCombinationInstances = combinationValuesGroupWithMostOccurences(combination,
                    deduplicatedCombinations);
            if (maxCombinationInstances.size() > globalMaxCombinationInstancesCount) {
                globalMaxCombinationIndex = combinationIndex;
                globalMaxCombinationInstancesCount = maxCombinationInstances.size();
                globalMaxCombinationInstances = (ArrayList<Integer>) maxCombinationInstances.clone();
            }
            combinationIndex++;
        }
        if (globalMaxCombinationIndex != -1) {
            optimalCombination = combinations.get
                    (globalMaxCombinationIndex);
            return globalMaxCombinationInstances;

        }
        return new ArrayList<>();

    }

    private ArrayList<Integer> combinationValuesGroupWithMostOccurences(Integer[] combination,
                                                                        ArrayList<ArrayList<Integer>>
                                                                                combinationValuesGroups) {
        int maxSize = -1;
        int maxValueGroupI = -1;
        for (int valueGroupI = 0; valueGroupI < combinationValuesGroups.size(); valueGroupI++) {
            if (combinationValuesGroups.get(valueGroupI).size() > maxSize) {
                maxSize = combinationValuesGroups.get(valueGroupI).size();
                maxValueGroupI = valueGroupI;
            }
        }
        if (maxValueGroupI != -1) {
            return combinationValuesGroups.get(maxValueGroupI);
        }
        return new ArrayList<>();
    }

    private ArrayList<ArrayList<Integer>> removeDuplicatesFromCombinationValuesGroups
            (Integer[] combination,
             ArrayList<ArrayList<Integer>> combinationValuesGroups,
             int mainPartitionIndex,
             ArrayList<ArrayList<Integer[]>> allPartitions) {

        ArrayList<Integer> duplicatedGroups = new ArrayList<>();
        ArrayList<Integer[]> mainPartition = allPartitions.get(mainPartitionIndex);
        for (int valueGroupI = 0; valueGroupI < combinationValuesGroups.size(); valueGroupI++) {
            Integer groupInstanceRepresentantIndex = combinationValuesGroups.get(valueGroupI).get(0);
            Integer[] groupValues = mainPartition.get(groupInstanceRepresentantIndex);
            boolean groupHasDuplicates = groupHasDuplicates(combination, mainPartitionIndex, allPartitions, groupValues);
            if (groupHasDuplicates) {
                duplicatedGroups.add(valueGroupI);
//                System.out.println("duplicatedGroup: " + valueGroupI);
            }
        }
        ArrayList<ArrayList<Integer>> deduplicatedCombinationValuesGroups = new ArrayList<>();
        for (int valueGroupI = 0; valueGroupI < combinationValuesGroups.size(); valueGroupI++) {
            boolean isDuplicatedGroup = isOnDuplicatedGroupsList(duplicatedGroups, valueGroupI);
            if (!isDuplicatedGroup) {
                deduplicatedCombinationValuesGroups.add(combinationValuesGroups.get(valueGroupI));
            }
        }
        return deduplicatedCombinationValuesGroups;
    }

    private boolean isOnDuplicatedGroupsList(ArrayList<Integer> duplicatedGroups, int groupIndex) {
        boolean isDuplicatedGroup = false;
        for (int duplicatedGroupI = 0; duplicatedGroupI < duplicatedGroups.size(); duplicatedGroupI++) {
            if (duplicatedGroups.get(duplicatedGroupI) == groupIndex) {
                isDuplicatedGroup = true;
                break;
            }
        }
        return isDuplicatedGroup;
    }

    private boolean groupHasDuplicates(Integer[] combination, int mainPartitionIndex,
                                       ArrayList<ArrayList<Integer[]>> allPartitions, Integer[] groupValues) {
        boolean groupHasDuplicates = false;
        outer_loop:
        for (int partitionI = 0; partitionI < allPartitions.size(); partitionI++) {
            if (partitionI == mainPartitionIndex)
                continue;
            ArrayList<Integer[]> partition = allPartitions.get(partitionI);
            for (int instanceI = 0; instanceI < partition.size(); instanceI++) {
                Integer[] currentInstanceValues = partition.get(instanceI);

                boolean instanceIsEqual = instancesEqualForTheCombination(combination, groupValues,
                        currentInstanceValues);
                if (instanceIsEqual) {
                    groupHasDuplicates = true;
                    break outer_loop;
                }
            }
        }
        return groupHasDuplicates;
    }

    private boolean instancesEqualForTheCombination(Integer[] combination, Integer[] instance1,
                                                    Integer[] instance2) {
        boolean isInstanceEqual = true;
        for (int attribute = 0; attribute < combination.length; attribute++) {
            if (instance1[combination[attribute]] != instance2[combination[attribute]]) {
                isInstanceEqual = false;
                break;
            }
        }
        return isInstanceEqual;
    }

    private ArrayList<ArrayList<Integer>> getCombinationValuesGroups(Integer[] combination, ArrayList<Integer[]> partition,
                                                                     boolean[] classifiedInstances) {

        ArrayList<ArrayList<Integer>> instancesGroupsWithEqualCombinationValues = new ArrayList<>();
        boolean[] instanceAlreadyAdded = new boolean[partition.size()];
        for (int instanceI = 0; instanceI < partition.size(); instanceI++) {
            if (instanceAlreadyAdded[instanceI] || classifiedInstances[instanceI])
                continue;
            Integer[] instance = partition.get(instanceI);
            ArrayList<Integer> instancesWithEqualValuesForCombination = getInstancesWithSameCombinationValues(combination,
                    instance, partition, classifiedInstances, instanceAlreadyAdded);
            instancesGroupsWithEqualCombinationValues.add(instancesWithEqualValuesForCombination);

        }

        return instancesGroupsWithEqualCombinationValues;
    }

    private ArrayList<Integer> getInstancesWithSameCombinationValues(Integer[] combination, Integer[] values,
                                                                     ArrayList<Integer[]> partition,
                                                                     boolean[] classifiedInstances, boolean[]
            instanceAlreadyAdded) {
        ArrayList<Integer> instancesWithValues = new ArrayList<Integer>();
        int instanceIndex = 0;
        for (Integer[] instanceValues : partition) {
            if (classifiedInstances[instanceIndex] || instanceAlreadyAdded[instanceIndex]) {
                instanceIndex++;
                continue;
            }
            boolean hasSameValues = true;
            for (Integer attribute : combination) {
                if (instanceValues[attribute] != values[attribute]) {
                    hasSameValues = false;
                    break;
                }
            }
            if (hasSameValues) {
                instanceAlreadyAdded[instanceIndex] = true;
                instancesWithValues.add(instanceIndex);
            }
            instanceIndex++;
        }
        return instancesWithValues;
    }

    private ArrayList<Integer[]> generateCombinations(int numberOfAttributes, int combinationSize) {
        ArrayList<Integer[]> combinations = new ArrayList<>();

        for (int globalCombination = 1; globalCombination < Math.pow(2, numberOfAttributes); ++globalCombination) {
            ArrayList<Integer> tmpCombination = new ArrayList<>();
            for (int currentBit = 0; currentBit < numberOfAttributes; currentBit++) {
                if ((globalCombination & (1 << currentBit)) > 0) {
                    tmpCombination.add(currentBit);
                }
            }
            if (tmpCombination.size() == combinationSize) {
                combinations.add(tmpCombination.toArray(new Integer[combinationSize]));
            }

        }
        for (Integer[] combination : combinations) {
        }
        return combinations;
    }
}
