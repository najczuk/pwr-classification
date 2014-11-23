package pl.najczuk.machine_learning.discretizers;

import pl.najczuk.machine_learning.instances.Attribute;
import pl.najczuk.machine_learning.instances.AttributeType;
import pl.najczuk.machine_learning.instances.Instance;
import pl.najczuk.machine_learning.instances.Instances;

import java.util.*;

/**
 * User: Adrian
 * Date: 11/9/2014
 * Time: 00:27
 */
public abstract class UnsupervisedDiscretizer {
    private Integer numberOfBins;
    private Instances inputInstances;

    protected UnsupervisedDiscretizer(Integer numberOfBins, Instances inputInstances) {
        setNumberOfBins(numberOfBins);
        setInputInstances(inputInstances);
    }


    public Integer getNumberOfBins() {
        return numberOfBins;
    }

    public void setNumberOfBins(Integer numberOfBins) {
        this.numberOfBins = numberOfBins;
    }

    public Instances getInputInstances() {
        return inputInstances;
    }

    public void setInputInstances(Instances inputInstances) {
        this.inputInstances = inputInstances;
    }

    public Instances discretizeNumericAttributes() {
        ArrayList<Attribute> originalAttributes = getInputInstances().getAttributes();
        ArrayList<Attribute> nominalAttributes = new ArrayList<>();
        ArrayList<ArrayList<String>> columnarNominalValues = new ArrayList<>();
        Instances inputInstances = getInputInstances();


        for (Attribute currentAttribute : originalAttributes) {
            System.out.println(currentAttribute.getName());
            if (currentAttribute.getType().equals(AttributeType.NUMERIC)) {

                ArrayList<Double> attributeValues = inputInstances.getNumericAttributeValues(currentAttribute);
                ArrayList<Double> copyOfAttrValues = (ArrayList<Double>) attributeValues.clone();
                ArrayList<ArrayList<Double>> intervalRanges = getIntervalRangesForNumericAttribute(copyOfAttrValues);
                nominalAttributes.add(createNominalAttributeFromIntervalRanges(currentAttribute.getName(), intervalRanges));
                columnarNominalValues.add(getNominalRepresentationOfNumericValues(attributeValues, intervalRanges));
            } else
                nominalAttributes.add(currentAttribute);
            System.out.println(nominalAttributes.get(nominalAttributes.size()-1).getNominalValuesMap());

        }

        Instances instances = createNominalInstancesFromColumnarRepresentation(nominalAttributes, originalAttributes,
                inputInstances.getInstances(), columnarNominalValues);
        System.out.println(instances);
        return instances; //TODO daj tu cos sensownego
    }

    private Instances createNominalInstancesFromColumnarRepresentation(
            ArrayList<Attribute> nominalAttributes, ArrayList<Attribute> originalAttributes,
            ArrayList<Instance> originalInstances,
            ArrayList<ArrayList<String>> columnarNominalValues) {

        ArrayList<ArrayList<String>> discretizedInstancesStringValues = new ArrayList<>();
        ArrayList<Attribute> discretizedAttributes = new ArrayList<>();
        Attribute currentAttribute;

        for (int origAttrIndex = 0, nominalAttributeIndex = 0; origAttrIndex < originalAttributes.size() &&
                nominalAttributeIndex < nominalAttributes.size(); origAttrIndex++) {
            currentAttribute = originalAttributes.get(origAttrIndex);
            if (currentAttribute.getType().equals(AttributeType.NUMERIC))
                discretizedAttributes.add(nominalAttributes.get(nominalAttributeIndex++));
            else
                discretizedAttributes.add(currentAttribute);
        }

        for (int currInstIndex = 0; currInstIndex < originalInstances.size(); currInstIndex++) {
            ArrayList<String> discretizedInstanceStringValues = new ArrayList<>();

            for (int origAttrIndex = 0, nominalAttributeIndex = 0; origAttrIndex < originalAttributes.size() &&
                    nominalAttributeIndex < nominalAttributes.size(); origAttrIndex++) {
                currentAttribute = originalAttributes.get(origAttrIndex);
                String attributeStringValue;

                if (currentAttribute.getType().equals(AttributeType.NUMERIC)) {
                    attributeStringValue = getStringRepresentationOfNumericAttribute(columnarNominalValues,
                            currInstIndex, nominalAttributeIndex);

                    nominalAttributeIndex++;
                } else {
                    attributeStringValue = getStringRepresentationOfNominalAttribute(originalInstances,
                            currentAttribute, currInstIndex, origAttrIndex);
                }

                discretizedInstanceStringValues.add(attributeStringValue);

            }
            discretizedInstancesStringValues.add(discretizedInstanceStringValues);
        }
        return new Instances(discretizedAttributes, discretizedInstancesStringValues);

    }

    private String getStringRepresentationOfNumericAttribute(ArrayList<ArrayList<String>> columnarNominalValues, int
            currInstIndex, int attrIndex) {
//        System.out.println("columnarNominalValues = [" + columnarNominalValues + "], currInstIndex = [" + currInstIndex + "], attrIndex = [" + attrIndex + "]");
        ArrayList<String> columnarAttributeValues= columnarNominalValues.get(attrIndex);
//        System.out.println(Arrays.toString(columnarAttributeValues.toArray()));
//        System.out.println(columnarAttributeValues.size());
//        System.out.println(currInstIndex);
//        System.out.println(attrIndex);
        String attrValue = columnarAttributeValues.get(currInstIndex);
        return attrValue;
    }

    private String getStringRepresentationOfNominalAttribute(ArrayList<Instance> originalInstances, Attribute currentAttribute, int currInstIndex, int origAttrIndex) {
        return currentAttribute.getNominalValue(originalInstances.get(currInstIndex)
                .getValues().get(origAttrIndex));
    }

    private ArrayList<String> getNominalRepresentationOfNumericValues(ArrayList<Double> attributeValues,
                                                                      ArrayList<ArrayList<Double>> intervalRanges) {
        ArrayList<String> nominalRepresentationStrings = new ArrayList<>();
        Double lowerRangeLimit, upperRangeLimit;
        for (Double attributeValue : attributeValues) {
            for (ArrayList<Double> intervalRange : intervalRanges) {

                lowerRangeLimit = intervalRange.get(0);
                upperRangeLimit = intervalRange.get(1);
                if (attributeValue >= lowerRangeLimit && attributeValue < upperRangeLimit) {
                    nominalRepresentationStrings.add(getNominalRangeStringRepresentation(intervalRange));
                    break;
//                    System.out.println(getNominalRangeStringRepresentation(intervalRange));
                }

            }

        }

        return nominalRepresentationStrings;
    }

    private Attribute createNominalAttributeFromIntervalRanges(String attributeName, ArrayList<ArrayList<Double>>
            intervalRanges) {
        ArrayList<String> nominalValues = new ArrayList<>();


        for (ArrayList<Double> currentIntervalRange : intervalRanges) {
            String nominalValue = getNominalRangeStringRepresentation(currentIntervalRange);
            nominalValues.add(nominalValue);
        }

        Attribute attribute = new Attribute(attributeName, nominalValues);
        return attribute;
    }

    private String getNominalRangeStringRepresentation(ArrayList<Double> intervalRange) {
        Double lowerRangeLimit, upperRangeLimit;
        lowerRangeLimit = intervalRange.get(0);
        upperRangeLimit = intervalRange.get(1);
        return "[" + lowerRangeLimit + "," + upperRangeLimit + ")";
    }

    protected abstract ArrayList<ArrayList<Double>> getIntervalRangesForNumericAttribute(ArrayList<Double>
                                                                                                attributeValues);

    protected Double getMinValue(ArrayList<Double> attributeValues) {
        return attributeValues.get(0);
    }

    protected Double getMaxValue(ArrayList<Double> attributeValues) {
        return attributeValues.get(attributeValues.size() - 1);
    }

    protected void sortNumericValues(ArrayList<Double> numericAttributeValues) {
        HashSet<Double> hashSet = new HashSet(numericAttributeValues);
        ArrayList<Double> arrayList = new ArrayList<>(hashSet);
        Collections.sort(arrayList, new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o1.compareTo(o2);
            }
        });
    }
}
