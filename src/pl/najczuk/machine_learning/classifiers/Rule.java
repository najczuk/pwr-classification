package pl.najczuk.machine_learning.classifiers;

import pl.najczuk.machine_learning.instances.Attribute;

import java.util.ArrayList;

/**
 * User: Adrian
 * Date: 11/16/2014
 * Time: 15:50
 */
public class Rule {
    private ArrayList<Attribute> ruleAttributes;
    private ArrayList<Float> ruleAttributeValues;

    public Rule(ArrayList<Attribute> ruleAttributes, ArrayList<Float> ruleAttributeValues) {
        this.ruleAttributes = ruleAttributes;
        this.ruleAttributeValues = ruleAttributeValues;
    }
}
