package pl.najczuk.classifiers.discretizers;

import pl.najczuk.classifiers.instances.Attribute;
import pl.najczuk.classifiers.instances.AttributeType;
import pl.najczuk.classifiers.instances.Instance;
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
    protected ArrayList<ArrayList<Float>> getIntervalRangesForNumericAttribute(ArrayList<Float> attributeValues) {
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

}
