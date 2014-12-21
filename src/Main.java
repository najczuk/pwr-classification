
import pl.najczuk.machine_learning.instances.Instances;
import pl.najczuk.machine_learning.readers.ArffReader;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        ArffReader arffReader = new ArffReader
                ("C:\\Users\\Adrian\\workspace\\pwr\\pwr-classification\\datasets\\weather.arff");
        Instances instances = arffReader.getInstances();
        System.out.println(Arrays.deepToString(instances.getValuesArray()));



    }
}
