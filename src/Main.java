import pl.najczuk.machine_learning.classifiers.ILAClassifier;
import pl.najczuk.machine_learning.discretizers.EqualFrequencyDiscretizer;
import pl.najczuk.machine_learning.discretizers.EqualWidthDiscretizer;
import pl.najczuk.machine_learning.instances.Instances;
import pl.najczuk.machine_learning.readers.ArffReader;

public class Main {

    public static void main(String[] args) {
        ArffReader arffReader= new ArffReader("D:\\workspace\\pwr\\pwr-classification\\datasets\\glass.arff");
        Instances instances = arffReader.getInstances();
//        System.out.println(arffReader.getInstances().getNumericAttributeValues(arffReader.getInstances()
//                .getAttributes().get(1)));
//        EqualWidthDiscretizer equalWidthDiscretizer = new EqualWidthDiscretizer(5,arffReader.getInstances());
        EqualWidthDiscretizer equalWidthDiscretizer = new EqualWidthDiscretizer(10,instances);
        Instances discretizedInstances = equalWidthDiscretizer.discretizeNumericAttributes();
        ILAClassifier ilaClassifier =new ILAClassifier(discretizedInstances);
    }
}
