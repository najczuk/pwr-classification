package pl.najczuk.classifiers;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Adrian
 * Date: 10/31/2014
 * Time: 21:42
 */
public class Instance {
    private ArrayList<Attribute> attributes;
    private ArrayList<Float> values;

    public Instance(ArrayList<Attribute> attributes, ArrayList<Object> values) {
        this.attributes = attributes;
        this.values = getFloatValuesFromObjects(values);

    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<Float> getValues() {
        return values;
    }

    public void setValues(ArrayList<Float> values) {
        this.values = values;
    }

    private ArrayList<Float> getFloatValuesFromObjects(ArrayList<Object> mixedValues){
        ArrayList<Float> floatValues = new ArrayList<Float>();
        int limit = attributes.size();
        for (int currentFloatIndex = 0; currentFloatIndex < limit; currentFloatIndex++) {
            setFloatValueForAttribute(mixedValues, floatValues, currentFloatIndex);
        }
        return floatValues;

    }

    private void setFloatValueForAttribute(ArrayList<Object> mixedValues, ArrayList<Float> floatValues, int currentFloatIndex) {
        if(attributes.get(currentFloatIndex).getType().equals(AttributeType.NUMERIC))
            floatValues.add(currentFloatIndex,(Float)(mixedValues.get(currentFloatIndex)));
        else if(attributes.get(currentFloatIndex).getType().equals(AttributeType.NOMINAL)){
            floatValues.add(currentFloatIndex,attributes.get(currentFloatIndex).getNumericValue((String)(mixedValues.get(currentFloatIndex))));
        }
        else{
            floatValues.add(currentFloatIndex,null);
        }
    }
}
