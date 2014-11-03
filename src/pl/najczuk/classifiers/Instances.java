package pl.najczuk.classifiers;

import java.util.ArrayList;

/**
 * User: Adrian
 * Date: 10/31/2014
 * Time: 21:45
 */
public class Instances {
    private ArrayList<Instance> instances;
    private ArrayList<Attribute> attributes;



    public Instances(ArrayList<Attribute> attributes, ArrayList<ArrayList<String>> stringValuesArray) {
        this.attributes = attributes;
        this.instances = getInstancesFromObjectTwoDimArray(stringValuesArray);
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

    private ArrayList<Instance> getInstancesFromObjectTwoDimArray(ArrayList<ArrayList<String>>stringValuesArray) {
        ArrayList<Instance> instances = new ArrayList<Instance>();
        int limit = stringValuesArray.size();
        for (int currentInstanceIndex = 0; currentInstanceIndex < limit; currentInstanceIndex++) {
              instances.add(currentInstanceIndex,new Instance(this.attributes,
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
