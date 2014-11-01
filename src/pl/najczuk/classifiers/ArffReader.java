package pl.najczuk.classifiers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * User: Adrian
 * Date: 10/31/2014
 * Time: 21:07
 */
public class ArffReader {
    private File file;
    ArrayList<String> columns, classes;
    Integer classColumnNumber;


    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(new FileReader("D:\\workspace\\pwr\\pwr-classification\\datasets\\glass.arff"));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            String attributePattern = "(?i)@attribute";
            while (in.hasNextLine()){
                line = in.nextLine();
                if(line.matches(attributePattern))
                    System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
