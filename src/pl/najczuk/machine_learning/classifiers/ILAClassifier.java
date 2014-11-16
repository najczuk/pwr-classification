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
        ArrayList<ArrayList<Float[]>> classSubArrays = new ArrayList<>();

        Instance currentInstance;
        ArrayList<Float> currentInstanceValues;
        Integer currentClassValue;
        int currentSubArrayIndex;

        for (int currentTrainingInstanceIndex = 0; currentTrainingInstanceIndex < trainingInstancesSize;
             currentTrainingInstanceIndex++) {
            currentInstance = trainingInstances.getInstances().get(currentTrainingInstanceIndex);
            currentInstanceValues = currentInstance.getValues();
            currentClassValue = Math.round(currentInstanceValues.get(classAttributeIndex));

            if (classToSubArrayIndexMap[currentClassValue] == null) {
                currentSubArrayIndex = getIndexOfNewSubarray(classSubArrays);
                classToSubArrayIndexMap[currentClassValue] = currentSubArrayIndex;
                ArrayList<Float[]> subArrayInstances = new ArrayList<>();
                subArrayInstances.add(arrayListToFloatArray(attributesCount, currentInstanceValues));
                classSubArrays.add(subArrayInstances);
            } else {
                currentSubArrayIndex = classToSubArrayIndexMap[currentClassValue];
                classSubArrays.get(currentSubArrayIndex).add(arrayListToFloatArray(attributesCount, currentInstanceValues));
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

    private Float[] arrayListToFloatArray(int attributesCount, ArrayList<Float> currentInstanceValues) {
        return currentInstanceValues.toArray(new Float[attributesCount]);
    }

    private int getIndexOfNewSubarray(ArrayList<ArrayList<Float[]>> classSubarrays) {
        return classSubarrays.size() - 1;
    }

    public Instances getTrainingInstances() {
        return trainingInstances;
    }

    public void setTrainingInstances(Instances trainingInstances) {
        this.trainingInstances = trainingInstances;
    }
}
