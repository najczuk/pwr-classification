package pl.najczuk.machine_learning.classifiers;


import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;

public class KNNClassifier extends Classifier {
    public static double EUCLIDEAN=2, MANHATTAN=1.0, CHEBYSHEV=100;

    public KNNClassifier(Instances trainingInstances) {
        super(trainingInstances);
    }

    @Override
    public ArrayList<Rule> trainClassifier() {
        return null;
    }

    @Override
    public Double classify(Instance instance) {
        return null;
    }



//    private void getDataArray
    private double calculateDistance(double[] i1, double[] i2, double p) {
        int limit = i1.length;
        double sum = 0;
        for (int i = 0; i < limit; i++) {
            sum += Math.pow(
                    Math.abs(i1[i] - i2[i]),
                    p);
        }
        return Math.pow(sum,p);
    }


}
