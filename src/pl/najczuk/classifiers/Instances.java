package pl.najczuk.classifiers;

import java.util.ArrayList;

/**
 * User: Adrian
 * Date: 10/31/2014
 * Time: 21:45
 */
public class Instances {
    ArrayList<Instance> instances;
    ArrayList<Attribute> attributes;

    public Instances(ArrayList<Attribute> attributes, ArrayList<ArrayList<Object>> objectValuesArray) {
        this.attributes = attributes;
    }

    private ArrayList<Instance> getInstancesFromObjectTwoDimArray(ArrayList<ArrayList<Object>> objectValuesArray) {
        ArrayList<Instance> instances = new ArrayList<Instance>();
        int limit = objectValuesArray.size();
        for (int currentInstanceIndex = 0; currentInstanceIndex < limit; currentInstanceIndex++) {
              instances.add(currentInstanceIndex,new Instance(this.attributes,objectValuesArray.get(currentInstanceIndex)));
        }
        return instances;

    }


}
