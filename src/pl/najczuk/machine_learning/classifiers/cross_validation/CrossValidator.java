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


    public CrossValidator(Instances instances, int numberOfFolds) {
        this.instances = instances;
        this.numberOfFolds = numberOfFolds;
        this.numberOfClasses = instances.getAttributes().get(instances.getAttributes().size() - 1)
                .getNominalValuesMap().size();
    }

    public void crossValidate() {
        double[] stats = new double[5];
        double counter = 0;
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
//                System.out.println(testInstance.getValues());
                testInstanceClass = testInstance.getValues().get(testInstance.getValues().size() - 1);
                classificationClass = classifier.classify(testInstance);
                confusionMatrix[testInstanceClass.intValue()][classificationClass.intValue()]++;
            }
            System.out.println("Iteracja: " + iteration);
//            System.out.println(Arrays.deepToString(confusionMatrix));
            double[] partial = averageConfusionStats(confusionMatrix);
            stats[0]+=partial[0];
            stats[1]+=partial[1];
            stats[2]+=partial[1];
            stats[3]+=partial[1];
            stats[4]+=partial[1];
            counter++;
        }
        System.out.println("Overall STATS");
        System.out.printf("Stats acc:%f tpRate:%f fpRate:%f precision:%f fScore:%f\n", stats[0]/counter,
                stats[1]/counter, stats[2]/counter,
                stats[3]/counter, stats[4]/counter);
//        getAverageValuesFromArrayCols(stats);
    }

    private double[] averageConfusionStats(double[][] confusionMatrix) {
        double[] stats=new double[5];
        for (int actualClass = 0; actualClass < confusionMatrix.length; actualClass++) {
            double tp = 0, tn = 0, fp = 0, fn = 0;
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

            stats[0] += tpRate;
            stats[1] += fpRate;
            stats[2] += acc;
            stats[3] += precision;
            stats[4] += fScore;

            System.out.printf("Class[%d] acc:%f tpRate:%f fpRate:%f precision:%f fScore:%f\n", actualClass, acc, tpRate,
                    fpRate, precision, fScore);
        }
        System.out.println("handy avg");
        System.out.printf("Class[] acc:%f tpRate:%f fpRate:%f precision:%f fScore:%f\n", stats[0]/confusionMatrix.length,
                stats[1]/confusionMatrix.length,
                stats[2]/confusionMatrix.length, stats[3]/confusionMatrix.length, stats[4]/confusionMatrix.length);
        return stats;

    }

    private double[] getAverageValuesFromArrayCols(double[][]array){
        System.out.println("Before AVG:" + Arrays.deepToString(array));
        double[] averages = new double[array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                averages[j]+=array[i][j];
            }
        }
        for (int i = 0; i < averages.length; i++) {
            averages[i]=averages[i]/array.length;
        }
        System.out.println("Average Confusion Table: "+Arrays.toString(averages));
        return averages;
    }

}
