import pl.najczuk.classifiers.readers.ArffReader;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ArffReader arffReader= new ArffReader("D:\\workspace\\pwr\\pwr-classification\\datasets\\glass.arff");
        System.out.println(arffReader.getInstances().getNumericAttributeValues(arffReader.getInstances()
                .getAttributes().get(1)));
    }
}
