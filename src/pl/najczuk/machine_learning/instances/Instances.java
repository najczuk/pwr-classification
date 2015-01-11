package pl.najczuk.machine_learning.instances;

import java.util.ArrayList;
import java.util.Arrays;

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

    public Double[][] getValuesArray(){
        int rows = instances.size();
        int cols = attributes.size();
        Double[][] valuesArray = new Double[rows][cols];


        ArrayList<Double> values;
        for (int i = 0; i < rows; i++) {
            valuesArray[i] = instances.get(i).getValues().toArray(new Double[cols]);
        }
        return valuesArray;
    }

    public void normalizeInstances(){

//        System.out.println(Arrays.deepToString(getValuesArray()));
        Double[][] normalizedValues = normalizeValues(getValuesArray());
//        System.out.println(Arrays.deepToString(normalizedValues));


        for (int i = 0; i < instances.size(); i++) {
            instances.get(i).setValues(new ArrayList<>(Arrays.asList(normalizedValues[i])));
        }

//        System.out.println(Arrays.deepToString(getValuesArray()));
    }
    private Double[][] normalizeValues(Double[][] values) {
        int rows = values.length;
        int cols = values[0].length - 1;
//        System.out.println(values[0].length);
        Double[][] normalizedValues = new Double[rows][cols + 1];
        Double[][] maxMins = new Double[cols][2];
        getMinMaxRange(values, rows, cols, maxMins);
//        normalized value(i) = (Value (i) - Min V)/(Max V - Min V)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                normalizedValues[i][j] = (values[i][j] - maxMins[j][0]) / (maxMins[j][1] - maxMins[j][0]);
            }
            normalizedValues[i][cols] = values[i][cols];
        }

        return normalizedValues; //TODO
    }
    private void getMinMaxRange(Double[][] values, int rows, int cols, Double[][] maxMins) {
//        System.out.println(cols);
        for (int i = 0; i < cols; i++) {
            maxMins[i][0] = values[0][i];
            maxMins[i][1] = values[0][i];
        }
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (values[i][j] > maxMins[j][1])
                    maxMins[j][1] = values[i][j];
                else if (values[i][j] < maxMins[j][0])
                    maxMins[j][0] = values[i][j];
            }
        }
//        System.out.println(Arrays.deepToString(maxMins));
    }
}
