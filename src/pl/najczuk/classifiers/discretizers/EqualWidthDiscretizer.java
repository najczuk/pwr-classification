package pl.najczuk.classifiers.discretizers;

import pl.najczuk.classifiers.instances.Attribute;
import pl.najczuk.classifiers.instances.AttributeType;
import pl.najczuk.classifiers.instances.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * User: Adrian
 * Date: 11/9/2014
 * Time: 00:28
 */
public class EqualWidthDiscretizer extends UnsupervisedDiscretizer {

    public EqualWidthDiscretizer(Integer numberOfBins, Instances inputInstances) {
        super(numberOfBins, inputInstances);
    }

    @Override
    public Instances discretizeNumericAttributes() {
        ArrayList<Attribute> attributes = getInputInstances().getAttributes();
        ArrayList<Attribute> nominalAttributes = new ArrayList<>();
        ArrayList<ArrayList<String>> columnarNominalValues = new ArrayList<>();

        for (Attribute currentAttribute : attributes) {
            if (currentAttribute.getType().equals(AttributeType.NUMERIC)) {

                ArrayList<Float> attributeValues = getInputInstances().getNumericAttributeValues(currentAttribute);
                ArrayList<ArrayList<Float>> intervalRanges = getIntervalRangesForNumericAttribute(attributeValues);
                nominalAttributes.add(createNominalAttributeFromIntervalRanges(currentAttribute.getName(), intervalRanges));
                columnarNominalValues.add(getNominalRepresentationOfNumericValues(attributeValues, intervalRanges));
            }
            else{
//                nominalAttributes.add(currentAttribute);
            }

        }
        return null; //TODO daj tu cos sensownego
    }

    private Instances createNominalInstancesFromColumnarRepresentation(ArrayList<ArrayList<String>>
                                                                               columnarNominalValues,
                                                                       ArrayList<Attribute> nominalAttributes){


        return null;

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
                    System.out.println(getNominalRangeStringRepresentation(intervalRange));
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

    private ArrayList<ArrayList<Float>> getIntervalRangesForNumericAttribute(ArrayList<Float> attributeValues) {
        ArrayList<ArrayList<Float>> intervalRanges = new ArrayList<>();
        sortNumericValues(attributeValues);
        Float globalMaxValue, globalMinValue, interval;
        globalMaxValue = getMaxValue(attributeValues);
        globalMinValue = getMinValue(attributeValues);
        interval = (globalMaxValue - globalMinValue) / getNumberOfBins();

        for (int currentBinIndex = 0; currentBinIndex < getNumberOfBins(); currentBinIndex++) {

            Float lowerRangeLimit = currentBinIndex == 0 ? Float.NEGATIVE_INFINITY : globalMinValue +
                    (currentBinIndex * interval);
            Float upperRangeLimit = currentBinIndex == getNumberOfBins() - 1 ? Float.POSITIVE_INFINITY : globalMinValue + (
                    (currentBinIndex + 1)
                            * interval);
            intervalRanges.add(new ArrayList<>(Arrays.asList(lowerRangeLimit, upperRangeLimit)));
        }

        return intervalRanges;
    }

    private Float getMinValue(ArrayList<Float> attributeValues) {
        return attributeValues.get(0);
    }

    private Float getMaxValue(ArrayList<Float> attributeValues) {
        return attributeValues.get(attributeValues.size() - 1);
    }

    private void sortNumericValues(ArrayList<Float> numericAttributeValues) {
        Collections.sort(numericAttributeValues, new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return o1.compareTo(o2);
            }
        });
    }
}
