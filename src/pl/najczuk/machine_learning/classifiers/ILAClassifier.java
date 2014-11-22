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
        ArrayList<ArrayList<Integer[]>> classPartitions = getILASubArrays(trainingInstances);
        printClassSubArrays(classPartitions);


    }

    private void printClassSubArrays(ArrayList<ArrayList<Integer[]>> classSubArrays) {
        for (int i = 0; i < classSubArrays.size(); i++) {
            for (int j = 0; j < classSubArrays.get(i).size(); j++) {
                System.out.println(Arrays.deepToString(classSubArrays.get(i).get(j)));
            }
            System.out.println();
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

    public void setTrainingInstances(Instances trainingInstances) {
        this.trainingInstances = trainingInstances;
    }
}
