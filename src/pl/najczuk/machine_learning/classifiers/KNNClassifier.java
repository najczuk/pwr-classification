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
    private Double[][] values;

    public KNNClassifier(Instances trainingInstances, int k, double minkowskiBase) {
        super(trainingInstances);
        this.k = k;
        int numOfAttributes = trainingInstances.getAttributes().size();
        numOfClasses = trainingInstances.getAttributes().get(numOfAttributes-1).getNominalValuesMap().size();
        this.minkowskiBase = minkowskiBase;
        values = trainingInstances.getValuesArray();
//        System.out.println(Arrays.deepToString(values));
    }

    public KNNClassifier(Instances trainingInstances) {
        super(trainingInstances);
    }

    @Override
    public ArrayList<Rule> trainClassifier() {
        return null;
    }


    public Double[][] getValues() {
        return values;
    }

    @Override
    public Double classify(Instance instance) {

        return getNearestNeighbour(instance.getValues().toArray(new Double[instance.getValues().size()]));
    }


    public Double getNearestNeighbour(Double[] instanceValues) {
        Double[][] classWithDistance = new Double[values.length][2];
        int classIndex = values[0].length - 1;
        for (int i = 0; i < values.length; i++) {
            Double[] classAndDistance = {values[i][classIndex], calculateDistance(values[i], instanceValues,
                    minkowskiBase, 1)};
            classWithDistance[i] = classAndDistance;
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
//        System.out.println("SORT " + Arrays.deepToString(classWithDistance));
        Double[][] subKArray = Arrays.copyOfRange(classWithDistance, 0, k);

//        System.out.println("Class with distance:" + Arrays.deepToString(subKArray));
        return getRankingLeader(subKArray);
    }

    public double getRankingLeader(Double[][] classWithDistance) {

        double[] classesCount = new double[numOfClasses];
        for (int i = 0; i < classWithDistance.length; i++) {
            classesCount[classWithDistance[i][0].intValue()]++;
        }

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
//        if (noUniqueLeader) {
//            return getRankingLeader(Arrays.copyOfRange(classWithDistance, 0, k - 1));
//        }
        return leaderIndex;


    }

    private double calculateDistance(Double[] i1, Double[] i2, double p, int offset) {
//        System.out.print("Distance" + Arrays.toString(i1) + "\n" + Arrays.toString(i2)+"\n");
        int limit = i1.length - offset;
        double sum = 0;
        for (int i = 0; i < limit; i++) {
            sum += Math.pow(
                    Math.abs(i1[i] - i2[i])
                    , p);
        }
        double result = Math.pow(sum, 1 / p);
//        System.out.println(result);
        return Math.pow(sum, 1 / p);
    }




}
