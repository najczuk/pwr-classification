package pl.najczuk.machine_learning.classifiers.cross_validation;

import pl.najczuk.machine_learning.classifiers.ILAClassifier;
import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Adrian
 * Date: 11/24/2014
 * Time: 01:39
 */
public class CrossValidator {
    private Instances instances;
    private int numberOfFolds, numberOfClasses;
    private Double tp, tn, fp, fn, accuracy, precision, fscore, tprate, fprate;


    public CrossValidator(Instances instances, int numberOfFolds) {
        this.instances = instances;
        this.numberOfFolds = numberOfFolds;
        this.numberOfClasses = instances.getAttributes().get(instances.getAttributes().size() - 1)
                .getNominalValuesMap().size();
    }

    public void crossValidate() {
        double[][] confusionMatrix;
        Instances trainingInstances, testInstances;
        InstancesFolder instancesFolder = new InstancesFolder(instances, numberOfFolds);
        ILAClassifier classifier;
        Double testInstanceClass,classificationClass;

        for (int iteration = 0; iteration < numberOfFolds; iteration++) {
            confusionMatrix = new double[numberOfClasses][numberOfClasses];
            testInstances = instancesFolder.getTestInstances(iteration);
            trainingInstances = instancesFolder.getTrainingInstances(iteration);
            ArrayList<Instance> testInstancesList = testInstances.getInstances();
            classifier = new ILAClassifier(trainingInstances);

            for(Instance testInstance:testInstancesList){
                System.out.println(testInstance.getValues());
                testInstanceClass = testInstance.getValues().get(testInstance.getValues().size() - 1);
                classificationClass = classifier.classify(testInstance);
                confusionMatrix[testInstanceClass.intValue()][classificationClass.intValue()]++;
            }
            System.out.println("Iteracja: " + iteration);
            System.out.println(Arrays.deepToString(confusionMatrix));
            tableOfConfusion(confusionMatrix);
        }
    }

    private void tableOfConfusion(double[][] confusionMatrix) {
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
            fScore = (2 * tp) / (2 * tp + fp + fn);


            tpRate= Double.isNaN(tpRate) ? 0:tpRate;
            fpRate= Double.isNaN(fpRate) ? 0:fpRate;
            acc= Double.isNaN(acc) ? 0:acc;
            precision= Double.isNaN(precision) ? 0:precision;
            fScore= Double.isNaN(fScore) ? 0:fScore;

            System.out.printf("Class[%d] acc:%f tpRate:%f fpRate:%f precision:%f fScore:%f\n", actualClass, acc, tpRate,
                    fpRate, precision, fScore);

        }

    }
}
