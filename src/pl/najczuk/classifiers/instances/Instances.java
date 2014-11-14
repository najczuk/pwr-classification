package pl.najczuk.classifiers.instances;

import java.util.ArrayList;

/**
 * User: Adrian
 * Date: 10/31/2014
 * Time: 21:45
 */
public class Instances {
    private ArrayList<Instance> instances;
    private ArrayList<Attribute> attributes;

    public Instances(ArrayList<Attribute> attributes, ArrayList inputArray) {
        setAttributes(attributes);
        if(inputArray.get(0).getClass().equals(ArrayList.class)){
            setInstances(getInstancesFromObjectTwoDimArray(inputArray));
        }
        else if(inputArray.get(0).getClass().equals(Instance.class)) {
            setInstances(inputArray);
        }

    }

    public ArrayList<Float> getNumericAttributeValues(Attribute attribute) {
        if (attribute.getType().equals(AttributeType.NUMERIC)) {
            ArrayList<Float> numericAttributeValues = getFloatValuesForAttribute(getAttributeIndex(attribute));
            return numericAttributeValues;
        }

        return null;
    }

    private ArrayList<Float> getFloatValuesForAttribute(Integer attributeIndex) {
        ArrayList<Float> numericAttributeValues = new ArrayList<Float>();
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

    public Integer getSize(){
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
        return "Instances{" +
                "instances=" + instances +
                ", attributes=" + attributes +
                '}';
    }
}
