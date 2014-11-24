package pl.najczuk.machine_learning.instances;

import java.util.ArrayList;

/**
 * User: Adrian
 * Date: 10/31/2014
 * Time: 21:45
 */
public class Instances {
    private ArrayList<Instance> instances;
    private ArrayList<Attribute> attributes;

    public Instances() {
        this.instances = instances;
        this.attributes = attributes;
    }

    public Instances(ArrayList<Attribute> attributes, ArrayList<ArrayList<String>> inputArray) {
        setAttributes(attributes);
        setInstances(getInstancesFromObjectTwoDimArray(inputArray));
    }

    public ArrayList<Double> getNumericAttributeValues(Attribute attribute) {
        if (attribute.getType().equals(AttributeType.NUMERIC)) {
            ArrayList<Double> numericAttributeValues = getDoubleValuesForAttribute(getAttributeIndex(attribute));
            return numericAttributeValues;
        }

        return null;
    }

    private ArrayList<Double> getDoubleValuesForAttribute(Integer attributeIndex) {
        ArrayList<Double> numericAttributeValues = new ArrayList<Double>();
        for (int i = 0; i < getInstances().size(); i++) {
            numericAttributeValues.add(instances.get(i).getValues().get(attributeIndex));
        }
        return numericAttributeValues;
    }

    private Integer getAttributeIndex(Attribute attribute) {
        for (int i = 0; i < getAttributes().size(); i++) {
            if (attribute.equals(getAttributes().get(i))) {
                return i;
            }
        }
        return null;
    }

    public Integer getSize() {
        return getInstances().size();
    }

    public ArrayList<Instance> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<Instance> instances) {
        this.instances = instances;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    private ArrayList<Instance> getInstancesFromObjectTwoDimArray(ArrayList<ArrayList<String>> stringValuesArray) {
        ArrayList<Instance> instances = new ArrayList<Instance>();
        int limit = stringValuesArray.size();
        for (int currentInstanceIndex = 0; currentInstanceIndex < limit; currentInstanceIndex++) {
            instances.add(currentInstanceIndex, new Instance(this.attributes,
                    stringValuesArray.get(currentInstanceIndex)));
        }
        return instances;

    }

    @Override
    public String toString() {
        int attributeIndex = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (Attribute attribute : getAttributes()) {
            stringBuilder.append(attribute.getName());
            stringBuilder.append("\t");
        }
        stringBuilder.append("\n");
        for (Instance instance : getInstances()) {
            attributeIndex = 0;
            for (Attribute attribute : getAttributes()) {
                if (attribute.getType().equals(AttributeType.NOMINAL))
                    stringBuilder.append(attribute.getNominalValue(instance.getValues().get(attributeIndex)));
                else
                    stringBuilder.append(instance.getValues().get(attributeIndex));
                stringBuilder.append("\t");
                attributeIndex++;
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
