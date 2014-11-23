package pl.najczuk.machine_learning.classifiers;

import pl.najczuk.machine_learning.instances.Attribute;
import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Adrian
 * Date: 11/16/2014
 * Time: 16:17
 */
public class ILAClassifier {
    Instances trainingInstances;
    ArrayList<Attribute> attributes;
    Integer[] optimalCombination;

    public ILAClassifier(Instances trainingInstances) {
        this.trainingInstances = trainingInstances;
        this.attributes = trainingInstances.getAttributes();
        trainClassifier();
    }


    private void trainClassifier() {
        Instances trainingInstances = getTrainingInstances();
        ArrayList<Attribute> attributes = trainingInstances.getAttributes();
        ArrayList<ArrayList<Integer[]>> classPartitions = getILASubArrays(trainingInstances);
        printClassSubArrays(classPartitions);
        getRules(classPartitions, attributes.size() - 1);


    }

    private void printClassSubArrays(ArrayList<ArrayList<Integer[]>> classSubArrays) {
        for (int i = 0; i < classSubArrays.size(); i++) {
            for (int j = 0; j < classSubArrays.get(i).size(); j++) {
                System.out.println(Arrays.deepToString(classSubArrays.get(i).get(j)));
            }
            System.out.println();
        }
    }

    private ArrayList<ArrayList<Integer[]>> getILASubArrays(Instances trainingInstances) {

        int trainingInstancesSize = trainingInstances.getSize();
        int attributesCount = trainingInstances.getAttributes().size();
        int classAttributeIndex = attributesCount - 1;
        Integer[] classToSubArrayIndexMap = new Integer[attributesCount];
        ArrayList<ArrayList<Integer[]>> classSubArrays = new ArrayList<>();

        int currentSubArrayIndex;
        Integer currentClassValue;
        Instance currentInstance;
        ArrayList<Double> currentInstanceValues;


        for (int currentTrainingInstanceIndex = 0; currentTrainingInstanceIndex < trainingInstancesSize;
             currentTrainingInstanceIndex++) {
            currentInstance = trainingInstances.getInstances().get(currentTrainingInstanceIndex);
            currentInstanceValues = currentInstance.getValues();
            currentClassValue = currentInstanceValues.get(classAttributeIndex).intValue();
            if (classToSubArrayIndexMap[currentClassValue] == null) {
                ArrayList<Integer[]> subArrayInstances = new ArrayList<>();
                subArrayInstances.add(doubleValuesToIntegerArray(attributesCount, currentInstanceValues));
                classSubArrays.add(subArrayInstances);

                currentSubArrayIndex = getIndexOfNewSubArray(classSubArrays);
                classToSubArrayIndexMap[currentClassValue] = currentSubArrayIndex;
            } else {
                currentSubArrayIndex = classToSubArrayIndexMap[currentClassValue];
                classSubArrays.get(currentSubArrayIndex).add(doubleValuesToIntegerArray(attributesCount, currentInstanceValues));
            }
        }
        return classSubArrays;
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

    public Instances getTrainingInstances() {
        return trainingInstances;
    }

    private void getRules(ArrayList<ArrayList<Integer[]>> partitions, int noOfAttributes) {
        ArrayList<Rule> rules = new ArrayList<>();
        for (int partitionI = 0; partitionI < partitions.size(); partitionI++) {
            System.out.println("------------PARTITION " + partitionI);
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
                    System.out.println(Arrays.toString(optimalCombination));
                    rules.add(generateRuleFromMaxCombination(partitions, partitionI, optimalCombination, maxCombinationInstances));
                }
                if (maxCombinationInstances.isEmpty()) {
                    attrSpan++;
                }
            }

        }

        System.out.println();
        System.out.println();
        for(Rule rule:rules){
            System.out.println(rule);
        }

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
        Rule rule = new Rule(attributes, maxCombination, ruleValues, instanceClassValue);
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
            System.out.println("---->For current span max combination is: " + Arrays.toString(combinations.get
                    (globalMaxCombinationIndex)) + " for instances: " + Arrays.deepToString(globalMaxCombinationInstances
                    .toArray()));
            optimalCombination = combinations.get
                    (globalMaxCombinationIndex);
            return globalMaxCombinationInstances;

        }
        System.out.println("NO GLOBAL MAX COM FOR THESE COMBINATIONS");
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
            System.out.println("Max combination: " + Arrays.toString(combination) + " Instances: " + Arrays.toString
                    (combinationValuesGroups.get(maxValueGroupI).toArray()));
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
                System.out.println("Deduplicated combination: " + Arrays.toString(combination) + " Values: " + Arrays
                        .toString(mainPartition.get(combinationValuesGroups.get(valueGroupI).get(0))) + " Instances: " +
                        Arrays.toString(combinationValuesGroups.get(valueGroupI).toArray()));
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

            System.out.println("Combination: " + Arrays.toString(combination) + " Values: " + Arrays.toString
                    (instance) + " Instances: " + Arrays.deepToString(instancesWithEqualValuesForCombination.toArray()));
        }

        return instancesGroupsWithEqualCombinationValues;
    }

    private ArrayList<Integer> getInstancesWithSameCombinationValues(Integer[] combination, Integer[] values,
                                                                     ArrayList<Integer[]> partition,
                                                                     boolean[] classifiedInstances, boolean[]
            instanceAlreadyAdded) {
        ArrayList<Integer> instancesWithValues = new ArrayList<>();
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
        System.out.println("numberOfAttributes = [" + numberOfAttributes + "], combinationSize = [" + combinationSize + "]");
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
        System.out.println("Combinations");
        for (Integer[] combination : combinations) {
            System.out.println(Arrays.toString(combination));
        }
        return combinations;
    }
}
