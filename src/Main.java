import pl.najczuk.classifiers.discretizers.EqualFrequencyDiscretizer;
import pl.najczuk.classifiers.discretizers.EqualWidthDiscretizer;
import pl.najczuk.classifiers.readers.ArffReader;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ArffReader arffReader= new ArffReader("D:\\workspace\\pwr\\pwr-classification\\datasets\\glass.arff");
//        System.out.println(arffReader.getInstances().getNumericAttributeValues(arffReader.getInstances()
//                .getAttributes().get(1)));
//        EqualWidthDiscretizer equalWidthDiscretizer = new EqualWidthDiscretizer(5,arffReader.getInstances());
        EqualFrequencyDiscretizer equalWidthDiscretizer = new EqualFrequencyDiscretizer(5,arffReader.getInstances());
        equalWidthDiscretizer.discretizeNumericAttributes();
    }
}
