package pl.najczuk.machine_learning.instances;

import java.util.ArrayList;

/**
 * User: Adrian
 * Date: 10/31/2014
 * Time: 21:42
 */
public class Instance {
    private ArrayList<Attribute> attributes;
    private ArrayList<Double> values;

    public Instance(ArrayList<Attribute> attributes, ArrayList values) {
        setAttributes( attributes);

        if(values.get(0).getClass().equals(Double.class)){
            setValues(values);
        }
        else if(values.get(0).getClass().equals(String.class)) {
            setValues(getDoubleValuesFromObjects(values));
        }

    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    public void setValues(ArrayList<Double> values) {
        this.values = values;
    }



    private ArrayList<Double> getDoubleValuesFromObjects(ArrayList<String> mixedValues) {
        ArrayList<Double> doubleValues = new ArrayList<>();
        int limit = getAttributes().size();
        for (int currentAttributeIndex = 0; currentAttributeIndex < limit; currentAttributeIndex++) {
            extractDoubleValueForAttribute(mixedValues, doubleValues, currentAttributeIndex);
        }
        return doubleValues;

    }

    private void extractDoubleValueForAttribute(ArrayList<String> mixedValues, ArrayList<Double> doubleValues,
                                                int currentAttributeIndex) {
        if (getAttributes().get(currentAttributeIndex).getType().equals(AttributeType.NUMERIC))
            doubleValues.add(currentAttributeIndex, Double.valueOf(mixedValues.get(currentAttributeIndex)));
        else if (getAttributes().get(currentAttributeIndex).getType().equals(AttributeType.NOMINAL)) {
            doubleValues.add(currentAttributeIndex, getAttributes().get(currentAttributeIndex).getNumericValue((
                    (mixedValues.get(currentAttributeIndex)))));
        } else {
            doubleValues.add(currentAttributeIndex, null);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        int index=0;
        for(Attribute attribute:attributes){
            stringBuilder.append(attribute.getName());
            stringBuilder.append("["+attribute.getNominalValue(getValues().get(index))+"] ");

                    index++;
        }
        return stringBuilder.toString();

    }
}
