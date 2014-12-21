package pl.najczuk.machine_learning.classifiers;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import pl.najczuk.machine_learning.instances.Attribute;
import pl.najczuk.machine_learning.instances.AttributeType;
import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;

/**
 * Created by Adrian on 29/11/2014.
 */
public class NBClassifier extends Classifier {
    private ArrayList<ArrayList<ArrayList<Double[]>>> classAttributesValuesMetrics;
    private ArrayList<ArrayList<Double[]>> partitions;
    private double[][][] partitionsAttributesValuesMerics;

    public NBClassifier(Instances trainingInstances) {
        super(trainingInstances);

        partitionsAttributesValuesMerics = getPartitionsAttributesMetrics();
    }

    public ArrayList<ArrayList<ArrayList<Double[]>>> getClassAttributesValuesMetrics() {
        return classAttributesValuesMetrics;
    }

    public void setClassAttributesValuesMetrics(ArrayList<ArrayList<ArrayList<Double[]>>> classAttributesValuesMetrics) {
        this.classAttributesValuesMetrics = classAttributesValuesMetrics;
    }

    @Override
    public ArrayList<Rule> trainClassifier() {
        return null;
    }

    @Override
    public Double classify(Instance instance) {
        int nonClassAttributesCount = instance.getAttributes().size() - 2;
        double[] values = ArrayUtils.toPrimitive(instance.getValues().toArray(new Double[nonClassAttributesCount]));
        int partitionsCount = partitions.size();
        double[] classProbabilities = new double[partitions.size()];

        for (int paritionI = 0; paritionI < partitionsCount; paritionI++) {
            double partitionProb = 1;
            for (int attributeI = 0; attributeI < getAttributes().size() - 1; attributeI++) {
                double attributeProb;
                if (getAttributes().get(attributeI).getType().equals(AttributeType.NOMINAL)) {
                    attributeProb = partitionsAttributesValuesMerics[paritionI][attributeI][(int) values[attributeI]];
                } else {
                    double mean = partitionsAttributesValuesMerics[paritionI][attributeI][1];
                    double variance = partitionsAttributesValuesMerics[paritionI][attributeI][0];
                    attributeProb = (1 / (Math.sqrt(2 * Math.PI * variance))) *
                            Math.exp(
                                    (-Math.pow((values[attributeI] - mean), 2)) /
                                            (2 * variance)
                            );

                }
                partitionProb *=attributeProb;
            }
            classProbabilities[paritionI] = partitionProb;
        }

        double maxVal = -1;
        int maxPartitionI = -1;
        for (int partitionI = 0; partitionI < classProbabilities.length; partitionI++) {
            if(classProbabilities[partitionI]>maxVal){
                maxVal=classProbabilities[partitionI];
                maxPartitionI=partitionI;
            }

        }
        return (double) maxPartitionI;
    }

    private double[][][] getPartitionsAttributesMetrics() {

        partitions = getDoublePartitions(getTrainingInstances());
        Instances instances = getTrainingInstances();
        ArrayList<Attribute> attributes = instances.getAttributes();
        ArrayList<Instance> instancesList = instances.getInstances();
        int numberOfPartitions = partitions.size();
        int numberOfAttributes = attributes.size() - 1; //last is class attribute

        double[][][] partitionsAttributesValuesMerics = initializePartitionsAttributesValuesCounts(attributes,
                numberOfPartitions, numberOfAttributes);
//        printPartitionsAttributesValuesCount(partitionsAttributesValuesCounts);
        calculatePartitionsAttributesValues(instancesList, attributes, numberOfAttributes,
                partitionsAttributesValuesMerics);
//        printPartitionsAttributesValuesCount(partitionsAttributesValuesCounts);
        return partitionsAttributesValuesMerics;

    }

    private void calculatePartitionsAttributesValues(ArrayList<Instance> instancesList,
                                                     ArrayList<Attribute> attributes, int numberOfAttributes,
                                                     double[][][] partitionsAttributesValuesCounts) {

        Instance instance;
        int instancePartition;
        int valueI;


        for (int instanceI = 0; instanceI < instancesList.size(); instanceI++) {
            instance = instancesList.get(instanceI);
            instancePartition = instance.getValues().get(instance.getValues().size() - 1).intValue();

            for (int attributeI = 0; attributeI < numberOfAttributes; attributeI++) {
                if (attributes.get(attributeI).getType().equals(AttributeType.NOMINAL)) {
                    valueI = instance.getValues().get(attributeI).intValue();
                    partitionsAttributesValuesCounts[instancePartition][attributeI][valueI]++;
                }
            }
        }
        Variance variance = new Variance();
        Mean mean = new Mean();
        ArrayList<Double> attributeValues;
        for (int attributeI = 0; attributeI < attributes.size(); attributeI++) {
            if (attributes.get(attributeI).getType().equals(AttributeType.NUMERIC)) {

                for (int partitionI = 0; partitionI < partitions.size(); partitionI++) {
                    attributeValues = new ArrayList<Double>();
                    for (int instanceI = 0; instanceI < instancesList.size(); instanceI++) {
                        instance = instancesList.get(instanceI);
                        instancePartition = instance.getValues().get(instance.getValues().size() - 1).intValue();
                        if (instancePartition == partitionI) {
                            attributeValues.add(instance.getValues().get(attributeI));
                        }
                    }
                    Double[] valuesArr = attributeValues.toArray(new Double[attributeValues.size()]);
//                    System.out.println(Arrays.toString(valuesArr));
                    double varianceVal = variance.evaluate(ArrayUtils.toPrimitive(valuesArr));
                    double meanVal = mean.evaluate(ArrayUtils.toPrimitive(valuesArr));
//                    System.out.printf("Var:%f Mean:%f\n", varianceVal,meanVal);
                    partitionsAttributesValuesCounts[partitionI][attributeI][0] = varianceVal;
                    partitionsAttributesValuesCounts[partitionI][attributeI][1] = meanVal;
                }
            }
        }

        for (int partitionI = 0; partitionI < partitions.size(); partitionI++) {
            for (int attributeI = 0; attributeI < numberOfAttributes; attributeI++) {

//                System.out.println(Arrays.toString(partitionsAttributesValuesCounts[partitionI][attributeI]));
                for (int attrValI = 0; attrValI < partitionsAttributesValuesCounts[partitionI][attributeI].length;
                     attrValI++) {
                    if (attributes.get(attributeI).getType().equals(AttributeType.NOMINAL))
                        partitionsAttributesValuesCounts[partitionI][attributeI][attrValI] /= partitions.get
                                (partitionI).size() + 1;
                }

//                System.out.println(Arrays.toString(partitionsAttributesValuesCounts[partitionI][attributeI]));
            }
        }

    }


    private double[][][] initializePartitionsAttributesValuesCounts(ArrayList<Attribute> attributes, int numberOfPartitions, int numberOfAttributes) {
        double[][][] partitionsAttributesValuesCounts;
        int[] numberOfAttributeValuesMetrics = new int[numberOfAttributes];
        for (int attributeI = 0; attributeI < numberOfAttributes; attributeI++) {
            if (attributes.get(attributeI).getType().equals(AttributeType.NUMERIC)) {
                numberOfAttributeValuesMetrics[attributeI] = 2; // slots for variance and mean
            } else
                numberOfAttributeValuesMetrics[attributeI] = attributes.get(attributeI).getNominalValuesMap().size();
        }
        partitionsAttributesValuesCounts = new double[numberOfPartitions][numberOfAttributes][];
        for (int partitionI = 0; partitionI < numberOfPartitions; partitionI++) {
            for (int attributeI = 0; attributeI < numberOfAttributes; attributeI++) {
                partitionsAttributesValuesCounts[partitionI][attributeI] = new
                        double[numberOfAttributeValuesMetrics[attributeI]];
                for (int valueI = 0; valueI < numberOfAttributeValuesMetrics[attributeI]; valueI++) {
//                    System.out.printf("Partition:%d AttributeI:%d ValI:%d \n", partitionI, attributeI, valueI);
                    if (attributes.get(attributeI).getType().equals(AttributeType.NOMINAL))
                        partitionsAttributesValuesCounts[partitionI][attributeI][valueI]++; // we put artificial 1
                    // count to avoid 0 probability later on
                }
//                System.out.println(Arrays.toString(partitionsAttributesValuesCounts[partitionI][attributeI]));
            }
        }


        return partitionsAttributesValuesCounts;
    }

    private void printPartitionsAttributesValuesCount(double[][][] partitionsAttributesValuesCounts) {
        int numberOfPartitions = partitionsAttributesValuesCounts.length;
        int numberOfAttributes = partitionsAttributesValuesCounts[0].length;
        for (int partitionI = 0; partitionI < numberOfPartitions; partitionI++) {
//            System.out.println("Partition " + partitionI);
            for (int attributeI = 0; attributeI < numberOfAttributes; attributeI++) {
//                System.out.println("Attribute: " + attributeI);
//                System.out.println(Arrays.toString(partitionsAttributesValuesCounts[partitionI][attributeI]));
            }
//            System.out.println();
        }
    }

}
