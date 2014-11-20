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

    public ILAClassifier(Instances trainingInstances) {
        this.trainingInstances = trainingInstances;
        trainClassifier();
    }


    private void trainClassifier() {
        Instances trainingInstances = getTrainingInstances();
        ArrayList<Attribute> attributes = trainingInstances.getAttributes();

        int subArrayCount;
        int trainingInstancesSize = trainingInstances.getSize();
        int attributesCount = attributes.size();
        int nonClassAttributesCount = attributesCount-1;
        int classAttributeIndex = attributesCount - 1;

        Integer[] classToSubArrayIndexMap = new Integer[attributesCount];
        ArrayList<ArrayList<Integer[]>> classSubArrays = new ArrayList<>();

        generateILASubArrays(trainingInstances, trainingInstancesSize, attributesCount, classAttributeIndex, classToSubArrayIndexMap, classSubArrays);
        subArrayCount = classSubArrays.size();

//        ------------------------------------------------------


        for (int subArrayIndex = 0; subArrayIndex < subArrayCount; subArrayIndex++) {
            ArrayList<Integer[]> subArray = classSubArrays.get(subArrayIndex);
            int instancesCount = subArray.size();
            int checkedInstancesCount =0;
            boolean[] checkedInstances = new boolean[instancesCount];
            int attributeCombinationSize=1;
            ArrayList<Integer[]> combinations;

            while (instancesCount==checkedInstancesCount) {
                combinations=generateCombinations(nonClassAttributesCount,attributeCombinationSize);
                int maxCombinationRank =0;
                Integer[] maxCombinationAttributes;
                Integer[] maxCombinationValues;


                for (int combinationIndex = 0; combinationIndex < combinations.size(); combinationIndex++) {
                    Integer[] combination =combinations.get(combinationIndex);
                    for (int instanceIndex = 0; instanceIndex < instancesCount; instanceIndex++) {
                        Integer[] instanceValues = subArray.get(instanceIndex);
                        Integer[] combinationValues = getValuesForCurrentCombination(attributeCombinationSize,
                                instanceValues,combination);
                    }

                }
            }


        }
    }

    private Integer[] getValuesForCurrentCombination(int attributeCombinationSize, Integer[] currentInstanceValues,
                                                     Integer[] attributesToEvaluate) {
        ArrayList<Integer> combinationValuesArray = new ArrayList<>();
        for (int attributeToEvaluateIndex = 0; attributeToEvaluateIndex < attributeCombinationSize;
             attributeToEvaluateIndex++) {
            combinationValuesArray.add(currentInstanceValues[attributesToEvaluate[attributeToEvaluateIndex]]);
        }

        return combinationValuesArray.toArray(new Integer[attributeCombinationSize]);
    }

    private ArrayList<Integer[]> generateCombinations(int numberOfAttributes,int combinationsSize){
        ArrayList<Integer[]> combinations = new ArrayList<>();

        for (int globalCombination = 1; globalCombination < Math.pow(2,numberOfAttributes); ++globalCombination) {
            ArrayList<Integer> tmpCombination = new ArrayList<>();
            for (int currentBit = 0; currentBit < numberOfAttributes; currentBit++) {
                if((globalCombination&(1<<currentBit))>0){
                    tmpCombination.add(currentBit);
                }
            }
            if(tmpCombination.size()==combinationsSize){
                combinations.add(tmpCombination.toArray(new Integer[combinationsSize]));
            }

        }
        return combinations;
    }

    private boolean checkIfCombinationIsUnique(Integer subArrayIndex, ArrayList<ArrayList<Integer[]>> classSubArrays,
                                               int subArrayCount,
                                               Integer[] combinationValues, Integer[] combinationAttrIndexes) {
        int subArrayInstancesCount;
        boolean isNotUnique = false;
        Integer[] currentInstanceValues;

        for (int currSubArrayIndex = 0; currSubArrayIndex < subArrayCount; currSubArrayIndex++) {
            if (currSubArrayIndex == subArrayIndex)
                continue;
            ArrayList<Integer[]> subsetInstanceValues = classSubArrays.get(currSubArrayIndex);
            subArrayInstancesCount = subsetInstanceValues.size();
            for (int currentInstance = 0; currentInstance < subArrayInstancesCount; currentInstance++) {
                currentInstanceValues = subsetInstanceValues.get(currentInstance);
                isNotUnique = true;
                for (int attributeIndex = 0; attributeIndex < combinationAttrIndexes.length; attributeIndex++) {
                    isNotUnique = isNotUnique &&
                            currentInstanceValues[combinationAttrIndexes[attributeIndex]] == combinationValues
                                    [attributeIndex];
                    if (isNotUnique) return true;
                }
            }
        }
        System.out.println("subArrayIndex = [" + subArrayIndex + "], classSubArrays = [" + classSubArrays + "], " +
                "subArrayCount = [" + subArrayCount + "], combintaionValues = [" + combinationValues + "], " +
                "combinationAttrIndexes = [" + combinationAttrIndexes + "], isNotUnique=[" + isNotUnique + "]");
        return isNotUnique;
    }

    private void generateILASubArrays(Instances trainingInstances, int trainingInstancesSize, int attributesCount,
                                      int classAttributeIndex, Integer[] classToSubArrayIndexMap,
                                      ArrayList<ArrayList<Integer[]>> classSubArrays) {
        Instance currentInstance;
        ArrayList<Double> currentInstanceValues;
        Integer currentClassValue;
        int currentSubArrayIndex;


        for (int currentTrainingInstanceIndex = 0; currentTrainingInstanceIndex < trainingInstancesSize;
             currentTrainingInstanceIndex++) {
            currentInstance = trainingInstances.getInstances().get(currentTrainingInstanceIndex);
            currentInstanceValues = currentInstance.getValues();
            currentClassValue = currentInstanceValues.get(classAttributeIndex).intValue();
//            System.out.println(classToSubArrayIndexMap[currentClassValue] + "-" + currentClassValue);
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

    public void setTrainingInstances(Instances trainingInstances) {
        this.trainingInstances = trainingInstances;
    }
}
