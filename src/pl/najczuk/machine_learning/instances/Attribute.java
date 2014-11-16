package pl.najczuk.machine_learning.instances;

import java.util.*;

/**
 * User: Adrian
 * Date: 10/31/2014
 * Time: 22:04
 */
public class Attribute {
    private String name;
    private AttributeType type;
    private HashMap<String,Float> nominalValuesMap;

    public Attribute(String name) {
        this.name = name;
        this.type = AttributeType.NUMERIC;
    }

    public Attribute(String name, ArrayList<String> nominalValuesArray) {
        this.name = name;
        this.type = AttributeType.NOMINAL;
        this.nominalValuesMap = getNominalValuesMapFromArray(nominalValuesArray);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public HashMap<String, Float> getNominalValuesMap() {
        return nominalValuesMap;
    }

    public void setNominalValuesMap(HashMap<String, Float> nominalValuesMap) {
        this.nominalValuesMap = nominalValuesMap;
    }

    public Float getNumericValue(String nominalValue){
        Float numericValue = nominalValuesMap.get(nominalValue);
        return numericValue;
    }

    public String getNominalValue(Float numericValue){
        Set entries = nominalValuesMap.entrySet();
        Iterator<Map.Entry<String,Float>> entriesIterator = entries.iterator();

        Map.Entry<String,Float> entry;
        while(entriesIterator.hasNext()){
            entry=entriesIterator.next();
            if(entry.getValue().equals(numericValue))
                return entry.getKey();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attribute)) return false;

        Attribute attribute = (Attribute) o;

        if (name != null ? !name.equals(attribute.name) : attribute.name != null) return false;
        if (nominalValuesMap != null ? !nominalValuesMap.equals(attribute.nominalValuesMap) : attribute.nominalValuesMap != null)
            return false;
        if (type != attribute.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", nominalValuesMap=" + nominalValuesMap +
                '}';
    }

    private HashMap<String,Float> getNominalValuesMapFromArray(ArrayList<String> nominalValuesArray){
        HashMap<String,Float> nominalValuesMap = new HashMap<String, Float>();
        Float nominalAttributeValue = new Float(0);
        for (String nominalValue:nominalValuesArray) {
            nominalValuesMap.put(nominalValue,nominalAttributeValue);
            nominalAttributeValue +=1;
        }
        return nominalValuesMap;
    }
}
