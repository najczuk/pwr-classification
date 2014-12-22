package pl.najczuk.machine_learning.classifiers;


import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;
import java.util.Arrays;

public class KNNClassifier extends Classifier {
    public static double EUCLIDEAN = 2, MANHATTAN = 1.0, CHEBYSHEV = 100;
    private int k, minkowskiBase;
    private Double[][] values, normalizedValues;

    public KNNClassifier(Instances trainingInstances, int k, int minkowskiBase) {
        super(trainingInstances);
        this.k = k;
        this.minkowskiBase = minkowskiBase;
        values = trainingInstances.getValuesArray();
        normalizedValues = normalizeValues(values);
    }

    public KNNClassifier(Instances trainingInstances) {
        super(trainingInstances);
    }

    @Override
    public ArrayList<Rule> trainClassifier() {
        return null;
    }

    public Double[][] getNormalizedValues() {
        return normalizedValues;
    }

    public Double[][] getValues() {
        return values;
    }

    @Override
    public Double classify(Instance instance) {
        return null;
    }


    //    private void getDataArray

    private void getNearestNeighbours(Double[] instanceValues){
                
    }

    private double calculateDistance(double[] i1, double[] i2, double p) {
        int limit = i1.length;
        double sum = 0;
        for (int i = 0; i < limit; i++) {
            sum += Math.pow(
                    Math.abs(i1[i] - i2[i]),
                    p);
        }
        return Math.pow(sum, p);
    }

    private Double[][] normalizeValues(Double[][] values) {
        int rows = values.length;
        int cols = values[0].length - 1;
//        System.out.println(values[0].length);
        Double[][] normalizedValues = new Double[rows][cols + 1];
        Double[][] maxMins = new Double[cols][2];
        getMinMaxRange(values, rows, cols, maxMins);
//        normalized value(i) = (Value (i) - Min V)/(Max V - Min V)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                normalizedValues[i][j] = (values[i][j] - maxMins[j][0]) / (maxMins[j][1] - maxMins[j][0]);
            }
            normalizedValues[i][cols] = values[i][cols];
        }

        return normalizedValues; //TODO
    }

    private void getMinMaxRange(Double[][] values, int rows, int cols, Double[][] maxMins) {
//        System.out.println(cols);
        for (int i = 0; i < cols; i++) {
            maxMins[i][0] = values[0][i];
            maxMins[i][1] = values[0][i];
        }
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (values[i][j] > maxMins[j][1])
                    maxMins[j][1] = values[i][j];
                else if (values[i][j] < maxMins[j][0])
                    maxMins[j][0] = values[i][j];
            }
        }
//        System.out.println(Arrays.deepToString(maxMins));
    }


}
