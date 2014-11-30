package pl.najczuk.machine_learning.classifiers;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import pl.najczuk.machine_learning.instances.Attribute;
import pl.najczuk.machine_learning.instances.AttributeType;
import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Adrian on 29/11/2014.
 */
public class NBClassifier extends Classifier {
    private ArrayList<ArrayList<ArrayList<Double[]>>> classAttributesValuesMetrics;
    private ArrayList<ArrayList<Double[]>> partitions;

    public NBClassifier(Instances trainingInstances) {
        super(trainingInstances);
    }

    public ArrayList<ArrayList<ArrayList<Double[]>>> getClassAttributesValuesMetrics() {
        return classAttributesValuesMetrics;
    }

    public void setClassAttributesValuesMetrics(ArrayList<ArrayList<ArrayList<Double[]>>> classAttributesValuesMetrics) {
        this.classAttributesValuesMetrics = classAttributesValuesMetrics;
    }

    @Override
    public ArrayList<Rule> trainClassifier() {
        getCountsForPartitionsAttributesValues();
        return null;
    }

    @Override
    public Double classify(Instance instance) {
        return null;
    }

    private void getCountsForPartitionsAttributesValues() {

        partitions = getDoublePartitions(getTrainingInstances());
        Instances instances = getTrainingInstances();
        ArrayList<Attribute> attributes = instances.getAttributes();
        ArrayList<Instance> instancesList = instances.getInstances();
        int numberOfPartitions = partitions.size();
        int numberOfAttributes = attributes.size() - 1; //last is class attribute

        double[][][] partitionsAttributesValuesCounts = initializePartitionsAttributesValuesCounts(attributes,
                numberOfPartitions, numberOfAttributes);
        printPartitionsAttributesValuesCount(partitionsAttributesValuesCounts);
        calculatePartitionsAttributesValues(instancesList, attributes, numberOfAttributes,
                partitionsAttributesValuesCounts);
        printPartitionsAttributesValuesCount(partitionsAttributesValuesCounts);


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
                valueI = instance.getValues().get(attributeI).intValue();
                if (attributes.get(attributeI).getType().equals(AttributeType.NOMINAL))
                    partitionsAttributesValuesCounts[instancePartition][attributeI][valueI]++;
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
                    partitionsAttributesValuesCounts[partitionI][attributeI][0]=varianceVal;
                    partitionsAttributesValuesCounts[partitionI][attributeI][1]=meanVal;
                }


            }
        }

    }


    private boolean isIntPresent(ArrayList<Integer> list, int integer) {
        for (Integer i : list) {
            if (i.intValue() == integer)
                return true;
        }
        return false;
    }

    private double[][][] initializePartitionsAttributesValuesCounts(ArrayList<Attribute> attributes, int numberOfPartitions, int numberOfAttributes) {
        double[][][] partitionsAttributesValuesCounts;
        int[] numberOfAttributeValuesMetrics = new int[numberOfAttributes];
        for (int attributeI = 0; attributeI < numberOfAttributes; attributeI++) {
            if (attributes.get(attributeI).getType().equals(AttributeType.NUMERIC)) {
                numberOfAttributeValuesMetrics[attributeI] = 2; // one slot for mean and one for variance
            } else
                numberOfAttributeValuesMetrics[attributeI] = attributes.get(attributeI).getNominalValuesMap().size();
        }
        partitionsAttributesValuesCounts = new double[numberOfPartitions][numberOfAttributes][];
        for (int partitionI = 0; partitionI < numberOfPartitions; partitionI++) {
            for (int attributeI = 0; attributeI < numberOfAttributes; attributeI++) {
                partitionsAttributesValuesCounts[partitionI][attributeI] = new
                        double[numberOfAttributeValuesMetrics[attributeI]];
                for (int attributeJ = 0; attributeJ < numberOfAttributeValuesMetrics[attributeI]; attributeJ++) {
                    if (!attributes.get(attributeJ).getType().equals(AttributeType.NUMERIC))
                        partitionsAttributesValuesCounts[partitionI][attributeI][attributeJ]++; // we put artificial 1
                    // count to avoid 0 probability later on
                }
            }
        }


        return partitionsAttributesValuesCounts;
    }

    private void printPartitionsAttributesValuesCount(double[][][] partitionsAttributesValuesCounts) {
        int numberOfPartitions = partitionsAttributesValuesCounts.length;
        int numberOfAttributes = partitionsAttributesValuesCounts[0].length;
        for (int partitionI = 0; partitionI < numberOfPartitions; partitionI++) {
            System.out.println("Partition " + partitionI);
            for (int attributeI = 0; attributeI < numberOfAttributes; attributeI++) {
                System.out.println("Attribute: " + attributeI);
                System.out.println(Arrays.toString(partitionsAttributesValuesCounts[partitionI][attributeI]));
            }
            System.out.println();
        }
    }

}
