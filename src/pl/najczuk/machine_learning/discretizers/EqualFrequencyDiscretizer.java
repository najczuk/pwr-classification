package pl.najczuk.machine_learning.discretizers;

import pl.najczuk.machine_learning.instances.Instances;

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

            Float lowerRangeLimit;
            Float upperRangeLimit;

//            intervalRanges.add(new ArrayList<>(Arrays.asList(lowerRangeLimit, upperRangeLimit)));
        }

        return intervalRanges; //TODO wychodzi malo binow dla niektorych atrybutow, sprawdz recznie jak to wychodzi
        // przy distinct
    }
}
