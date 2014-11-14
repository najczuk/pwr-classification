package pl.najczuk.classifiers.discretizers;

import pl.najczuk.classifiers.instances.Instances;

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

    public abstract Instances discretizeNumericAttributes();

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
}
