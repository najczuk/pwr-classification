package pl.najczuk.machine_learning.classifiers;

import pl.najczuk.machine_learning.instances.Attribute;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Adrian
 * Date: 11/16/2014
 * Time: 15:50
 */
public class Rule {
    ArrayList<Attribute> attributes;
    private Integer[] attributesIndexes;
    private Double[] values;
    private Double classAttributeValue;


    public Rule(ArrayList<Attribute> attributes, Integer[] attributesIndexes, Double[] values, Double classAttributeValue) {
        this.attributes = attributes;
        this.attributesIndexes = attributesIndexes;
        this.values = values;
        this.classAttributeValue = classAttributeValue;
    }

    public Rule(ArrayList<Attribute> attributes, Integer[] attributesIndexes, Integer[] values, Double
            classAttributeValue) {
        this.attributes = attributes;
        this.attributesIndexes = attributesIndexes;
        this.classAttributeValue = classAttributeValue;
        this.values = convertToDouble(values);
    }

    private Double[] convertToDouble(Integer[] integerValues) {
        Double[] doubleValues = new Double[integerValues.length];
        for (int valueI = 0; valueI < integerValues.length; valueI++) {
            doubleValues[valueI] = integerValues[valueI].doubleValue();
        }
        return doubleValues;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("IF ");
        for (int attributeI = 0; attributeI < attributesIndexes.length; attributeI++) {
//            System.out.println(attributes.get(attributesIndexes[attributeI]).getName());
            stringBuilder.append(attributes.get(attributesIndexes[attributeI]).getName());
            stringBuilder.append(" = ("+values[attributeI]+")");
            stringBuilder.append(attributes.get(attributesIndexes[attributeI]).getNominalValue(values[attributeI]));
            stringBuilder.append(" ");
        }
        stringBuilder.append("THEN ");
        stringBuilder.append(attributes.get(attributes.size()-1).getNominalValue(classAttributeValue));

        return stringBuilder.toString();
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public Integer[] getAttributesIndexes() {
        return attributesIndexes;
    }

    public Double[] getValues() {
        return values;
    }

    public Double getClassAttributeValue() {
        return classAttributeValue;
    }
}
