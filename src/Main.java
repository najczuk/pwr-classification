import com.sun.org.apache.xpath.internal.SourceTree;
import pl.najczuk.machine_learning.classifiers.ILAClassifier;
import pl.najczuk.machine_learning.classifiers.cross_validation.CrossValidator;
import pl.najczuk.machine_learning.discretizers.EqualFrequencyDiscretizer;
import pl.najczuk.machine_learning.discretizers.EqualWidthDiscretizer;
import pl.najczuk.machine_learning.discretizers.UnsupervisedDiscretizer;
import pl.najczuk.machine_learning.instances.Instances;
import pl.najczuk.machine_learning.readers.ArffReader;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ArffReader arffReader;
        Instances instances;
        UnsupervisedDiscretizer discretizer;
        Instances discretizedInstances;
        ILAClassifier ilaClassifier;
        CrossValidator crossValidator;
        String cvStats;

        System.out.println("dataSet, discretizer, kparameter, numberOfRules, folds, accuracy, tpRate, fpRate, " +
                "precision, fScore");

        String[] discretizers = {"EWD", "EFD"};
        String[] dataSets = {"GLASS", "IRIS", "WEATHER"};
//        String[] dataSets = {"WEATHER"};
        int[] kPars = {1,4,6,9,13,16,20};
        int[] foldCounts = {2, 5, 10, 15};

        for (String dataSet : dataSets) {
            String filePath;
            if (dataSet.equals("GLASS")) {
                filePath = "D:\\workspace\\pwr\\pwr-classification\\datasets\\glass.arff";
            } else if (dataSet.equals("IRIS")) {
                filePath = "D:\\workspace\\pwr\\pwr-classification\\datasets\\iris.arff";
            } else
                filePath = "D:\\workspace\\pwr\\pwr-classification\\datasets\\weather.arff";
            arffReader = new ArffReader(filePath);
            instances = arffReader.getInstances();
            for (int kPar : kPars) {
            for (String discretizerName : discretizers) {
                if (discretizerName.equals("EWD"))
                    discretizer = new EqualWidthDiscretizer(kPar, instances);
                else
                    discretizer = new EqualFrequencyDiscretizer(kPar,instances);
                discretizedInstances = discretizer.discretizeNumericAttributes();
//                System.out.println(discretizedInstances);

                    for (int folds : foldCounts) {
                        crossValidator = new CrossValidator(discretizedInstances, folds);
                        cvStats = crossValidator.crossValidate();
                        System.out.println(dataSet + ", " + discretizerName + ", " + kPar + ", " + cvStats);

                    }
                }

            }

        }

//        arffReader = new ArffReader
//                ("D:\\workspace\\pwr\\pwr-classification\\datasets\\weather.arff");
//        instances = arffReader.getInstances();
//        discretizer = new EqualWidthDiscretizer(kPar, instances);
//        discretizedInstances = discretizer.discretizeNumericAttributes();
//        ilaClassifier = new ILAClassifier(discretizedInstances);
//        crossValidator = new CrossValidator(discretizedInstances, folds);
//        cvStats = crossValidator.crossValidate();
//        System.out.println(dataSet + ", " + discretizerName + ", " + kPar + ", " + cvStats);


    }
}
