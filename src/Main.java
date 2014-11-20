import pl.najczuk.machine_learning.classifiers.ILAClassifier;
import pl.najczuk.machine_learning.discretizers.EqualFrequencyDiscretizer;
import pl.najczuk.machine_learning.discretizers.EqualWidthDiscretizer;
import pl.najczuk.machine_learning.instances.Instances;
import pl.najczuk.machine_learning.readers.ArffReader;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        generateCombinations(4,1);
        generateCombinations(4,2);
        generateCombinations(4,3);
        generateCombinations(4,4);
//        ArffReader arffReader= new ArffReader("D:\\workspace\\pwr\\pwr-classification\\datasets\\ila_decision.arff");
//        Instances instances = arffReader.getInstances();
////        System.out.println(arffReader.getInstances().getNumericAttributeValues(arffReader.getInstances()
////                .getAttributes().get(1)));
////        EqualWidthDiscretizer equalWidthDiscretizer = new EqualWidthDiscretizer(5,arffReader.getInstances());
//        EqualWidthDiscretizer equalWidthDiscretizer = new EqualWidthDiscretizer(10,instances);
//        Instances discretizedInstances = equalWidthDiscretizer.discretizeNumericAttributes();
//        ILAClassifier ilaClassifier =new ILAClassifier(discretizedInstances);


    }

    public static ArrayList<Integer[]> generateCombinations(int numberOfAttributes,int combinationsSize){
        ArrayList<Integer[]> combinations = new ArrayList<>();

        for (int globalCombination = 1; globalCombination < Math.pow(2,numberOfAttributes); ++globalCombination) {
            ArrayList<Integer> tmpCombination = new ArrayList<>();
            for (int currentBit = 0; currentBit < numberOfAttributes; currentBit++) {
                if((globalCombination&(1<<currentBit))>0){
                    tmpCombination.add(currentBit);
                }
            }
            if(tmpCombination.size()==combinationsSize){
                combinations.add(tmpCombination.toArray(new Integer[combinationsSize]));
            }

        }
        return combinations;
    }
}
