package pl.najczuk.classifiers.discretizers;

import pl.najczuk.classifiers.instances.Instances;

/**
 * User: Adrian
 * Date: 11/9/2014
 * Time: 00:29
 */
public class EqualFrequencyDiscretizer extends UnsupervisedDiscretizer {

    protected EqualFrequencyDiscretizer(Integer numberOfBins, Instances inputInstances) {
        super(numberOfBins, inputInstances);
    }

    @Override
    public Instances discretizeNumericAttributes() {
        return null;
    }
}
