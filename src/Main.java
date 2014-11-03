import com.sun.deploy.util.StringUtils;
import pl.najczuk.classifiers.ArffReader;
import pl.najczuk.classifiers.Attribute;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        ArffReader arffReader= new ArffReader("D:\\workspace\\pwr\\pwr-classification\\datasets\\glass.arff");
    }
}
