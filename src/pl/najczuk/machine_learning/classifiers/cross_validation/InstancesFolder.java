package pl.najczuk.machine_learning.classifiers.cross_validation;

import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Adrian
 * Date: 11/23/2014
 * Time: 23:49
 */
public class InstancesFolder {
    private Instances instances;
    private int numberOfFolds;
    private ArrayList<Instance> trainingInstances,testInstances;

    public InstancesFolder(Instances instances, int numberOfFolds) {
        this.instances = instances;
        this.numberOfFolds = numberOfFolds;
    }

    public Instances getTrainingInstances(int iteration){
        setFolds(iteration);
        Instances trainingInstances = new Instances();
        trainingInstances.setAttributes(instances.getAttributes());
        trainingInstances.setInstances(this.trainingInstances);
        return trainingInstances;
    }
    public Instances getTestInstances(int iteration){
        setFolds(iteration);
        Instances testInstances = new Instances();
        testInstances.setAttributes(instances.getAttributes());
        testInstances.setInstances(this.testInstances);
        return testInstances;
    }

    private void setFolds(int iteration){
        ArrayList<Instance> allInstances = instances.getInstances();
        ArrayList<Instance> trainingInstances = new ArrayList<>();
        ArrayList<Instance> testInstances = new ArrayList<>();
        int instancesCount = allInstances.size();
        int foldSize = instancesCount/numberOfFolds;
        int lowerLimit = foldSize*iteration;
        int upperLimit = foldSize*iteration+foldSize;
        Iterator<Instance> instancesIter = allInstances.iterator();
        int iteratorCounter = 0;
        System.out.println(lowerLimit + "< " +upperLimit + " foldsize:"+foldSize + "instances count:" + instancesCount);
        while (instancesIter.hasNext()){
            Instance instance = instancesIter.next();
            if(iteratorCounter>=lowerLimit && iteratorCounter<upperLimit)
                testInstances.add(instance);
            else
                trainingInstances.add(instance);
            iteratorCounter++;
        }
        this.testInstances=testInstances;
        this.trainingInstances = trainingInstances;

    }

    public int getNumberOfFolds() {
        return numberOfFolds;
    }

    public Instances getInstances() {
        return instances;
    }
}
