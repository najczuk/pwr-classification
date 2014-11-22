import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
//        generateCombinations(3,3);
//        generateCombinations(4,2);
//        generateCombinations(4,3);
//        generateCombinations(4,4);
        Integer[][] parititon1 = {{0, 0, 0, 0}, {1, 1, 2, 0}, {2, 2, 3, 0}, {2, 2, 2, 0}, {2, 2, 2, 0}};
        Integer[][] parititon2 = {{1, 1, 1, 1}, {2, 1, 1, 1}, {2, 1, 3, 1}};

        ArrayList<Integer[]> par1 = new ArrayList<>();
        par1.add(parititon1[0]);
        par1.add(parititon1[1]);
        par1.add(parititon1[2]);
        par1.add(parititon1[3]);
        par1.add(parititon1[4]);
        ArrayList<Integer[]> par2 = new ArrayList<>();
        par2.add(parititon2[0]);
        par2.add(parititon2[1]);
        par2.add(parititon2[2]);

        ArrayList<ArrayList<Integer[]>> partitions = new ArrayList<>();
        partitions.add(par1);
        partitions.add(par2);

        printClassSubArrays(partitions);


        //------getInstancesWithSameCombinationValues
//        Integer[] attributes = {0,1};
//        Integer[] values = {2,1};
//        boolean[] classifiedInstances = {false,true,false,false};
//        ArrayList<Integer> instancesWithValues = getInstancesWithSameCombinationValues(attributes, values, partitions.get
//                (1),classifiedInstances);
//        for (int i = 0; i < instancesWithValues.size(); i++) {
//            System.out.println(i + ": " + instancesWithValues.get(i));
//        }
        //getCombinationValesGroups
        int mainPartitionIndex = 0;
        Integer[] combination = {0};
        boolean[] classifiedInstances = {false, false, false, false, false};
        ArrayList<ArrayList<Integer>> combinationValuesGroups = getCombinationValuesGroups(combination,
                partitions.get(mainPartitionIndex), classifiedInstances);
        removeDuplicatesFromCombinationValuesGroups(combination, combinationValuesGroups,mainPartitionIndex,partitions);

//        ArrayList<Integer[]> combinations = generateCombinations(3, 1);


//        ArffReader arffReader= new ArffReader("D:\\workspace\\pwr\\pwr-classification\\datasets\\ila_decision.arff");
//        Instances instances = arffReader.getInstances();
//        EqualWidthDiscretizer equalWidthDiscretizer = new EqualWidthDiscretizer(10,instances);
//        Instances discretizedInstances = equalWidthDiscretizer.discretizeNumericAttributes();
//        ILAClassifier ilaClassifier =new ILAClassifier(discretizedInstances);


    }

    public static ArrayList<ArrayList<Integer>> removeDuplicatesFromCombinationValuesGroups
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
            }
        }
        ArrayList<ArrayList<Integer>> deduplicatedCombinationValuesGroups = new ArrayList<>();
        for (int valueGroupI = 0; valueGroupI < combinationValuesGroups.size(); valueGroupI++) {
            boolean isDuplicatedGroup = isOnDuplicatedGroupsList(duplicatedGroups, valueGroupI);
            if (!isDuplicatedGroup) {
                deduplicatedCombinationValuesGroups.add(combinationValuesGroups.get(valueGroupI));
                System.out.println("Deduplicated combination: " + Arrays.toString(combination) + " Values: " + Arrays
                        .toString
                        (mainPartition.get(combinationValuesGroups.get(valueGroupI).get(0))) + " Instances: "  +
                                Arrays.toString(combinationValuesGroups.get(valueGroupI).toArray()) );
            }
        }
        return deduplicatedCombinationValuesGroups;
    }

    private static boolean isOnDuplicatedGroupsList(ArrayList<Integer> duplicatedGroups, int groupIndex) {
        boolean isDuplicatedGroup = false;
        for (int duplicatedGroupI = 0; duplicatedGroupI < duplicatedGroups.size(); duplicatedGroupI++) {
            if (duplicatedGroups.get(duplicatedGroupI) == groupIndex) {
                isDuplicatedGroup = true;
                break;
            }
        }
        return isDuplicatedGroup;
    }

    private static boolean groupHasDuplicates(Integer[] combination, int mainPartitionIndex,
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

    private static boolean instancesEqualForTheCombination(Integer[] combination, Integer[] instance1,
                                                           Integer[] instance2) {
        boolean isInstanceEqual = true;
        for (int attribute = 0; attribute < combination.length; attribute++) {
            if (instance1[attribute] != instance2[attribute]) {
                isInstanceEqual = false;
                break;
            }
        }
        return isInstanceEqual;
    }

    public static ArrayList<ArrayList<Integer>> getCombinationValuesGroups(Integer[] combination, ArrayList<Integer[]> partition,
                                                                           boolean[] classifiedInstances) {

        ArrayList<ArrayList<Integer>> instancesGroupsWithEqualCombinationValues = new ArrayList<>();
        boolean[] instanceAlreadyAdded = new boolean[partition.size()];
        for (int instanceI = 0; instanceI < partition.size(); instanceI++) {
            if (instanceAlreadyAdded[instanceI])
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

    public static ArrayList<Integer> getInstancesWithSameCombinationValues(Integer[] combination, Integer[] values,
                                                                           ArrayList<Integer[]> partition,
                                                                           boolean[] classifiedInstances, boolean[]
            instanceAlreadyAdded) {
        ArrayList<Integer> instancesWithValues = new ArrayList<>();
        int instanceIndex = 0;
        for (Integer[] instanceValues : partition) {
            if (classifiedInstances[instanceIndex]) {
                instanceIndex++;
                continue;
            }
            boolean hasSameValues = true;
            int attributeIndex = 0;
            for (Integer attribute : combination) {
                if (instanceValues[attribute] != values[attributeIndex]) {
                    hasSameValues = false;
                    break;
                }
                attributeIndex++;
            }
            if (hasSameValues) {
                instanceAlreadyAdded[instanceIndex] = true;
                instancesWithValues.add(instanceIndex);
            }
            instanceIndex++;
        }
        return instancesWithValues;
    }

    public static void printClassSubArrays(ArrayList<ArrayList<Integer[]>> classSubArrays) {
        for (int i = 0; i < classSubArrays.size(); i++) {
            for (int j = 0; j < classSubArrays.get(i).size(); j++) {
                System.out.println(Arrays.deepToString(classSubArrays.get(i).get(j)));
            }
            System.out.println();
        }
    }

    public static ArrayList<Integer[]> generateCombinations(int numberOfAttributes, int combinationSize) {
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
