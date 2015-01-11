package pl.najczuk.machine_learning.classifiers;


import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class KNNClassifier extends Classifier {
    public static double EUCLIDEAN = 2, MANHATTAN = 1.0, CHEBYSHEV = 100;
    private int k, numOfClasses;
    private double minkowskiBase;
    private Double[][] values, normalizedValues;

    public KNNClassifier(Instances trainingInstances, int k, double minkowskiBase) {
        super(trainingInstances);
        this.k = k;
        int numOfAttributes = trainingInstances.getAttributes().size();
        numOfClasses = trainingInstances.getAttributes().get(numOfAttributes-1).getNominalValuesMap().size();
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

        return getNearestNeighbour(instance.getValues().toArray(new Double[instance.getValues().size()]));
    }


    public Double getNearestNeighbour(Double[] instanceValues) {
        Double[][] classWithDistance = new Double[normalizedValues.length][2];
        int classIndex = normalizedValues[0].length - 1;
        for (int i = 0; i < normalizedValues.length; i++) {
            Double[] values = {normalizedValues[i][classIndex], calculateDistance(normalizedValues[i], instanceValues,
                    minkowskiBase, 1)};
            classWithDistance[i] = values;
        }
//        System.out.println(Arrays.deepToString(classWithDistance));

        Arrays.sort(classWithDistance, new Comparator<Double[]>() {
            @Override
            public int compare(Double[] o1, Double[] o2) {
                Double distance1 = o1[1];
                Double distance2 = o2[1];
                return distance1.compareTo(distance2);
            }
        });
        System.out.println("SORT " + Arrays.deepToString(classWithDistance));
        Double[][] subKArray = Arrays.copyOfRange(classWithDistance, 0, k);

        System.out.println("Class with distance:" + Arrays.deepToString(subKArray));
        return getRankingLeader(subKArray);
    }

    public double getRankingLeader(Double[][] classWithDistance) {

        double[] classesCount = new double[numOfClasses];
        for (int i = 0; i < classWithDistance.length; i++) {
            classesCount[classWithDistance[i][0].intValue()]++;
        }
        System.out.println("Classes count:" + Arrays.toString(classesCount));

        int leaderIndex = -1;
        int leaderCount = -1;
        boolean noUniqueLeader = false;

        for (int i = 0; i < numOfClasses; i++) {
            if (classesCount[i] > leaderCount) {
                leaderIndex = i;
                classesCount[i]++;
                leaderCount = (int) classesCount[i];
                noUniqueLeader = false;
            } else if (classesCount[i] == leaderCount) {
                noUniqueLeader = true;
            }
        }
//        System.out.println(leaderIndex);
        if (noUniqueLeader) {
//            System.out.println("no unique leader");
            return getRankingLeader(Arrays.copyOfRange(classWithDistance, 0, k - 1));
        }
        return leaderIndex;


    }

    private double calculateDistance(Double[] i1, Double[] i2, double p, int offset) {
        System.out.print("Distance" + Arrays.toString(i1) + "\n" + Arrays.toString(i2)+"\n");
        int limit = i1.length - offset;
        double sum = 0;
        for (int i = 0; i < limit; i++) {
            sum += Math.pow(
                    Math.abs(i1[i] - i2[i])
                    , p);
        }
        double result = Math.pow(sum, 1 / p);
        System.out.println(result);
        return Math.pow(sum, 1 / p);
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
