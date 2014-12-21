package pl.najczuk.machine_learning.classifiers;


import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;

public class KNNClassifier extends Classifier {
    public KNNClassifier(Instances trainingInstances) {
        super(trainingInstances);
    }

    @Override
    public ArrayList<Rule> trainClassifier() {
        train();
        return null;
    }

    @Override
    public Double classify(Instance instance) {
        return null;
    }

    private void train(){
        

    }



}
