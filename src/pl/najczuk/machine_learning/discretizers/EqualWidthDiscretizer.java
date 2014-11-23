package pl.najczuk.machine_learning.discretizers;

import pl.najczuk.machine_learning.instances.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
    protected ArrayList<ArrayList<Double>> getIntervalRangesForNumericAttribute(ArrayList<Double> attributeValues) {
        ArrayList<ArrayList<Double>> intervalRanges = new ArrayList<>();
        Collections.min(attributeValues);
        Double globalMaxValue, globalMinValue, interval;
        globalMaxValue =
                Collections.max(attributeValues);
        globalMinValue =
                Collections.min(attributeValues);
        System.out.println(globalMinValue + " < " +globalMaxValue);
        interval = (globalMaxValue - globalMinValue) / getNumberOfBins();

        for (int currentBinIndex = 0; currentBinIndex < getNumberOfBins(); currentBinIndex++) {

            Double lowerRangeLimit = currentBinIndex == 0 ? Double.NEGATIVE_INFINITY : globalMinValue +
                    (currentBinIndex * interval);
            Double upperRangeLimit = currentBinIndex == getNumberOfBins() - 1 ? Double.POSITIVE_INFINITY : globalMinValue + (
                    (currentBinIndex + 1)
                            * interval);
            intervalRanges.add(new ArrayList<>(Arrays.asList(lowerRangeLimit, upperRangeLimit)));
        }

        return intervalRanges;
    }

}
