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
    protected ArrayList<ArrayList<Double>> getIntervalRangesForNumericAttribute(ArrayList<Double> attributeValues) {
        ArrayList<ArrayList<Double>> intervalRanges = new ArrayList<>();
//        sortNumericValues(attributeValues);

        Set set = new HashSet<>((ArrayList<Double>)attributeValues.clone());
        ArrayList<Double> distinctValues = new ArrayList<>(set);
        Collections.sort(distinctValues);
        System.out.println(Arrays.toString(distinctValues.toArray()));

        boolean partitioningEnded = false;
        int elementsPerPartition = getNumberOfBins();
        Iterator<Double> valuesIterator = distinctValues.iterator();
        int firstIndex=0;
        int lastIndex = distinctValues.size()-1;
        int globalElementIndex =0;
        while (!partitioningEnded) {
            ArrayList<Double> bin = new ArrayList<>();
            int currentElement = 0;
            while (valuesIterator.hasNext() && currentElement < elementsPerPartition) {

                Double nextValue = valuesIterator.next();
                if (globalElementIndex==firstIndex)
                    bin.add(Double.NEGATIVE_INFINITY);
                else if(globalElementIndex==lastIndex)
                    bin.add(Double.POSITIVE_INFINITY);
                else
                    bin.add(nextValue);
//                valuesIterator.next();
                globalElementIndex++;
                currentElement++;
            }
            Double lastBinMax = intervalRanges.size() !=0 ?intervalRanges.get(intervalRanges.size()-1).get(1):
                    Collections.min(bin);
            Double max = Collections.max(bin);
            Double min = lastBinMax;
            ArrayList<Double> interval = new ArrayList<>();
            interval.add(min); interval.add(max);
            intervalRanges.add(interval);
            partitioningEnded = !valuesIterator.hasNext();
            System.out.println("Bin: " + Arrays.toString(interval.toArray()));

        }


        return intervalRanges;
    }
}
