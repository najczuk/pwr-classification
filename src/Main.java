
import pl.najczuk.machine_learning.classifiers.KNNClassifier;
import pl.najczuk.machine_learning.instances.Instances;
import pl.najczuk.machine_learning.readers.ArffReader;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        ArffReader arffReader = new ArffReader
                ("D:\\workspace\\pwr\\pwr-classification\\datasets\\glass.arff");
        Instances instances = arffReader.getInstances();
        System.out.println(Arrays.deepToString(instances.getValuesArray()));
        KNNClassifier classifier = new KNNClassifier(instances,2,2);

        System.out.println(Arrays.deepToString(classifier.getNormalizedValues()));



    }
}
