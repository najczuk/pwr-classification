package pl.najczuk.machine_learning.classifiers;

import pl.najczuk.machine_learning.instances.Attribute;
import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;

/**
 * Created by Adrian on 29/11/2014.
 */
public abstract class Classifier {

    private Instances trainingInstances;
    private ArrayList<Attribute> attributes;
    private ArrayList<Rule> rules;

    public Classifier(Instances trainingInstances) {
        this.trainingInstances = trainingInstances;
        this.attributes = trainingInstances.getAttributes();
        this.rules = trainClassifier();
    }

    public abstract ArrayList<Rule> trainClassifier();
    public abstract Double classify(Instance instance);

    public Instances getTrainingInstances() {
        return trainingInstances;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public void setRules(ArrayList<Rule> rules) {
        this.rules = rules;
    }

    protected ArrayList<ArrayList<Integer[]>> getIntegerPartitions(Instances trainingInstances) {

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

                currentSubArrayIndex = classSubArrays.size()-1;
                classToSubArrayIndexMap[currentClassValue] = currentSubArrayIndex;
            } else {
                currentSubArrayIndex = classToSubArrayIndexMap[currentClassValue];
                classSubArrays.get(currentSubArrayIndex).add(doubleValuesToIntegerArray(attributesCount, currentInstanceValues));
            }
        }
        return classSubArrays;
    }

    protected ArrayList<ArrayList<Double[]>> getDoublePartitions(Instances trainingInstances) {

        int trainingInstancesSize = trainingInstances.getSize();
        int attributesCount = trainingInstances.getAttributes().size();
        int classAttributeIndex = attributesCount - 1;
        Integer[] classToSubArrayIndexMap = new Integer[attributesCount];
        ArrayList<ArrayList<Double[]>> classSubArrays = new ArrayList<>();

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
                ArrayList<Double[]> subArrayInstances = new ArrayList<>();
                subArrayInstances.add(currentInstanceValues.toArray(new
                        Double[currentInstanceValues.size()]));
                classSubArrays.add(subArrayInstances);

                currentSubArrayIndex = classSubArrays.size()-1;
                classToSubArrayIndexMap[currentClassValue] = currentSubArrayIndex;
            } else {
                currentSubArrayIndex = classToSubArrayIndexMap[currentClassValue];
                classSubArrays.get(currentSubArrayIndex).add(currentInstanceValues.toArray(new
                        Double[currentInstanceValues.size()]));
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

    protected void printPartitions(ArrayList<ArrayList<Integer[]>> classSubArrays) {
        for (int i = 0; i < classSubArrays.size(); i++) {
            for (int j = 0; j < classSubArrays.get(i).size(); j++) {
            }
        }
    }
}
