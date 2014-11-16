package pl.najczuk.classifiers.discretizers;

import pl.najczuk.classifiers.instances.Attribute;
import pl.najczuk.classifiers.instances.AttributeType;
import pl.najczuk.classifiers.instances.Instance;
import pl.najczuk.classifiers.instances.Instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

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
            if (currentAttribute.getType().equals(AttributeType.NUMERIC)) {

                ArrayList<Float> attributeValues = inputInstances.getNumericAttributeValues(currentAttribute);
                ArrayList<Float> copyOfAttrValues = (ArrayList<Float>) attributeValues.clone();
                ArrayList<ArrayList<Float>> intervalRanges = getIntervalRangesForNumericAttribute(copyOfAttrValues);
                nominalAttributes.add(createNominalAttributeFromIntervalRanges(currentAttribute.getName(), intervalRanges));
                columnarNominalValues.add(getNominalRepresentationOfNumericValues(attributeValues, intervalRanges));
            } else
                nominalAttributes.add(currentAttribute);

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
        return columnarNominalValues.get(attrIndex).get(currInstIndex);
    }

    private String getStringRepresentationOfNominalAttribute(ArrayList<Instance> originalInstances, Attribute currentAttribute, int currInstIndex, int origAttrIndex) {
        return currentAttribute.getNominalValue(originalInstances.get(currInstIndex)
                .getValues().get(origAttrIndex));
    }

    private ArrayList<String> getNominalRepresentationOfNumericValues(ArrayList<Float> attributeValues,
                                                                      ArrayList<ArrayList<Float>> intervalRanges) {
        ArrayList<String> nominalRepresentationStrings = new ArrayList<>();
        Float lowerRangeLimit, upperRangeLimit;
        for (Float attributeValue : attributeValues) {
            for (ArrayList<Float> intervalRange : intervalRanges) {

                lowerRangeLimit = intervalRange.get(0);
                upperRangeLimit = intervalRange.get(1);
                if (attributeValue >= lowerRangeLimit && attributeValue < upperRangeLimit) {
                    nominalRepresentationStrings.add(getNominalRangeStringRepresentation(intervalRange));
//                    System.out.println(getNominalRangeStringRepresentation(intervalRange));
                }

            }

        }

        return nominalRepresentationStrings;
    }

    private Attribute createNominalAttributeFromIntervalRanges(String attributeName, ArrayList<ArrayList<Float>>
            intervalRanges) {
        ArrayList<String> nominalValues = new ArrayList<>();


        for (ArrayList<Float> currentIntervalRange : intervalRanges) {
            String nominalValue = getNominalRangeStringRepresentation(currentIntervalRange);
            nominalValues.add(nominalValue);
        }

        Attribute attribute = new Attribute(attributeName, nominalValues);
        return attribute;
    }

    private String getNominalRangeStringRepresentation(ArrayList<Float> intervalRange) {
        Float lowerRangeLimit, upperRangeLimit;
        lowerRangeLimit = intervalRange.get(0);
        upperRangeLimit = intervalRange.get(1);
        return "[" + lowerRangeLimit + "," + upperRangeLimit + ")";
    }

    protected abstract ArrayList<ArrayList<Float>> getIntervalRangesForNumericAttribute(ArrayList<Float>
                                                                                                attributeValues);

    protected Float getMinValue(ArrayList<Float> attributeValues) {
        return attributeValues.get(0);
    }

    protected Float getMaxValue(ArrayList<Float> attributeValues) {
        return attributeValues.get(attributeValues.size() - 1);
    }

    protected void sortNumericValues(ArrayList<Float> numericAttributeValues) {
        HashSet<Float> hashSet = new HashSet(numericAttributeValues);
        ArrayList<Float> arrayList = new ArrayList<>(hashSet);
        Collections.sort(arrayList, new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return o1.compareTo(o2);
            }
        });
    }
}
