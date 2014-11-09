package pl.najczuk.classifiers.discretizers;

import pl.najczuk.classifiers.instances.Instances;

/**
 * User: Adrian
 * Date: 11/9/2014
 * Time: 00:27
 */
public interface Discretizer {
    public Instances discretizeNumericalAttributes(Instances inputInstances);
}
