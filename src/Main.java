
import pl.najczuk.machine_learning.classifiers.KNNClassifier;
import pl.najczuk.machine_learning.classifiers.cross_validation.CrossValidator;
import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;
import pl.najczuk.machine_learning.readers.ArffReader;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ArffReader arffReader;
        Instances instances;
//        String stats;
//        KNNClassifier classifier;
//        String[] datasets = {"iris.arff"};
//        int[] ks = {2, 3, 4, 5, 6, 7, 8, 9, 10};
//        int[] folds = {2, 3, 5, 8};
//        double[] metrics = {KNNClassifier.EUCLIDEAN, KNNClassifier.MANHATTAN, KNNClassifier.CHEBYSHEV};
//
//        for (int datasetI = 0; datasetI < datasets.length; datasetI++) {
//            arffReader = new ArffReader
//                    ("datasets\\" + datasets[datasetI]);
//            instances = arffReader.getInstances();
//            for (int kIndex = 0; kIndex < ks.length; kIndex++) {
//                for (int metricIndex = 0; metricIndex < metrics.length; metricIndex++) {
//                   classifier = new KNNClassifier(instances, ks[kIndex], metrics[metricIndex]);
//                    for (int foldIndex = 0; foldIndex < folds.length; foldIndex++) {
//                        CrossValidator validator = new CrossValidator(instances,folds[foldIndex]);
//                        stats = validator.crossValidateKNN(ks[kIndex], metrics[metricIndex]);
//                        System.out.println(datasets[datasetI]+","+metrics[metricIndex]+ "," + folds[foldIndex] +","+
//                                stats);
//                    }
//
//                }
//            }
//
//        }

        arffReader = new ArffReader
                ("datasets\\iris.arff");
        instances = arffReader.getInstances();
//        System.out.println(Arrays.deepToString(instances.getValuesArray()));
        KNNClassifier classifier = new KNNClassifier(instances, 10, 2);

//        System.out.println(Arrays.deepToString(classifier.getNormalizedValues()));
//
//        System.out.println(classifier.getNearestNeighbour(instances.getInstances().get(3).getValues()
//                .toArray(new Double[instances.getAttributes().size()])));


        for (int i = 0; i < 1; i++) {

            Instance testInstance = instances.getInstances().get(i);
            double classified = classifier.classify(testInstance);
            System.out.println(testInstance.getValues().get(testInstance.getValues().size()-1)+":"+classified);
        }


    }
}
