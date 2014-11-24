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


        ArffReader arffReader = new ArffReader
                ("D:\\workspace\\pwr\\pwr-classification\\datasets\\iris.arff");
        Instances instances = arffReader.getInstances();
        UnsupervisedDiscretizer discretizer = new EqualWidthDiscretizer(10, instances);
        Instances discretizedInstances = discretizer.discretizeNumericAttributes();
        ILAClassifier ilaClassifier = new ILAClassifier(discretizedInstances);
        CrossValidator crossValidator = new CrossValidator(discretizedInstances,10);
        crossValidator.crossValidate();

//        double[][] confusionMatrix = {{5, 3, 0}, {2, 3, 1}, {0, 2, 11}};
//        tableOfConfusion(confusionMatrix);


    }

    public static void tableOfConfusion(double[][] confusionMatrix) {
        for (int actualClass = 0; actualClass < confusionMatrix.length; actualClass++) {
            double tp = 0, tn = 0, fp = 0, fn = 0, p = 0, n = 0;
            for (int i = 0; i < confusionMatrix.length; i++) {
                for (int j = 0; j < confusionMatrix.length; j++) {
                    if (i == actualClass && j == actualClass)
                        tp += confusionMatrix[i][j];
                    if (i == actualClass && j != actualClass)
                        fn += confusionMatrix[i][j];
                    if (i != actualClass && j == actualClass)
                        fp += confusionMatrix[i][j];
                    if (i != actualClass && j != actualClass)
                        tn += confusionMatrix[i][j];
                    if (i == actualClass)
                        p += confusionMatrix[i][j];
                    if (i != actualClass)
                        n += confusionMatrix[i][j];
                }
            }
            double acc, tpRate, fpRate, precision, fScore;
            tpRate = tp / (tp + fn);
            fpRate = fp / (fp + tn);
            acc = (tp + tn) / ((tp + fn) + (fp + tn));
            precision = tp / (tp + fp);
            precision= Double.isNaN(precision) ? 0:precision;
            fScore = (2 * tp) / (2 * tp + fp + fn);
            fScore= Double.isNaN(fScore) ? 0:fScore;

            System.out.printf("Class[%d] acc:%f tpRate:%f fpRate:%f precision:%f fScore:%f\n", actualClass, acc, tpRate,
                    fpRate, precision, fScore);

        }

    }

}
