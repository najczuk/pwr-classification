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
        int subsetsCount = 0;
        Instances trainingInstances = getTrainingInstances();
        int trainingInstancesSize = trainingInstances.getSize();
        ArrayList<Attribute> attributes = trainingInstances.getAttributes();
        int attributesCount = attributes.size();
        int classAttributeIndex = attributesCount - 1;

        Integer[] classToSubArrayIndexMap = new Integer[attributesCount];
//        ArrayList<Integer> subArraysClassValues = new ArrayList<>();
        ArrayList<ArrayList<Double[]>> classSubArrays = new ArrayList<>();

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
                ArrayList<Double[]> subArrayInstances = new ArrayList<>();
                subArrayInstances.add(arrayListToDoubleArray(attributesCount, currentInstanceValues));
                classSubArrays.add(subArrayInstances);

                currentSubArrayIndex = getIndexOfNewSubArray(classSubArrays);
                classToSubArrayIndexMap[currentClassValue] = currentSubArrayIndex;
            } else {
                currentSubArrayIndex = classToSubArrayIndexMap[currentClassValue];
                classSubArrays.get(currentSubArrayIndex).add(arrayListToDoubleArray(attributesCount, currentInstanceValues));
            }
        }
        subsetsCount=classSubArrays.size();

        for (int i = 0; i < subsetsCount; i++) {
            System.out.println(i);
            for (int j = 0; j < classSubArrays.get(i).size(); j++) {
                System.out.println(Arrays.toString(classSubArrays.get(i).get(j)));
            }
        }

    }

    private Double[] arrayListToDoubleArray(int attributesCount, ArrayList<Double> currentInstanceValues) {
        return currentInstanceValues.toArray(new Double[attributesCount]);
    }

    private int getIndexOfNewSubArray(ArrayList<ArrayList<Double[]>> classSubArrays) {
        return classSubArrays.size() - 1;
    }

    public Instances getTrainingInstances() {
        return trainingInstances;
    }

    public void setTrainingInstances(Instances trainingInstances) {
        this.trainingInstances = trainingInstances;
    }
}
