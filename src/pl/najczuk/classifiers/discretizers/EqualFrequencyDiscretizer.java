package pl.najczuk.classifiers.discretizers;

import pl.najczuk.classifiers.instances.Instances;

import java.util.*;

/**
 * User: Adrian
 * Date: 11/9/2014
 * Time: 00:29
 */
public class EqualFrequencyDiscretizer extends UnsupervisedDiscretizer {

    public EqualFrequencyDiscretizer(Integer numberOfBins, Instances inputInstances) {
        super(numberOfBins, inputInstances);
    }

    @Override
    protected ArrayList<ArrayList<Float>> getIntervalRangesForNumericAttribute(ArrayList<Float> attributeValues) {
        ArrayList<ArrayList<Float>> intervalRanges = new ArrayList<>();
        sortNumericValues(attributeValues);
        int frequency;
        frequency = attributeValues.size() / getNumberOfBins();
        int numberOfBins = attributeValues.size()/frequency;

        for (int currentBinIndex = 0; currentBinIndex < numberOfBins; currentBinIndex++) {

            Float lowerRangeLimit = currentBinIndex == 0 ? Float.NEGATIVE_INFINITY : attributeValues.get
                    (currentBinIndex*frequency);
            Float upperRangeLimit = currentBinIndex == getNumberOfBins() - 1 ? Float.POSITIVE_INFINITY : attributeValues.get
                    ((currentBinIndex+1)*frequency);
//                            * interval);

            intervalRanges.add(new ArrayList<>(Arrays.asList(lowerRangeLimit, upperRangeLimit)));
        }

        return intervalRanges;
    }
}
