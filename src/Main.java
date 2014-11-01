import com.sun.deploy.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        try {
            Scanner in = new Scanner(new FileReader("D:\\workspace\\pwr\\pwr-classification\\datasets\\glass.arff"));
            String line;

            StringBuilder attributes = new StringBuilder();
            StringBuilder nominalAttributes = new StringBuilder();
            StringBuilder numericAttributes = new StringBuilder();

            Pattern numericAttributePattern = Pattern.compile("^@attribute\\s+.*\\s+real", Pattern.CASE_INSENSITIVE);
            Pattern nominalAttributePattern = Pattern.compile("^@attribute\\s+.*\\s+\\{.*\\}", Pattern.CASE_INSENSITIVE);
            Pattern nominallAttributeValuesPattern = Pattern.compile("\\{(.*?)\\}");

//            String nominalAttributeValues;
            Matcher nominalAttributeValuesMatcher;

//            boolean b = m.matches();

//            String attributePattern = "^(?i)@attribute";
            while (in.hasNextLine()) {
                line = in.nextLine();

                if (numericAttributePattern.matcher(line).matches()) {
                    System.out.println("NUMERIC ATRIBUTE: " + line);
                } else if (nominalAttributePattern.matcher(line).matches()) {
                    System.out.println("NOMINAL ATTRIBUTE: " + line);
//                    nominallAttributeValuesPattern.matcher(line).group();
                    nominalAttributeValuesMatcher = nominallAttributeValuesPattern.matcher(line);
                    if(nominalAttributeValuesMatcher.find()) {
                        System.out.println(nominalAttributeValuesMatcher.group(1));
                        String[] nominalAttributeValues = nominalAttributeValuesMatcher.group(1).split(",");
                        for (int i = 0; i < nominalAttributeValues.length; i++) {

                            System.out.println("NOMINAL ATTR VALUE: " + StringUtils.trimWhitespace(nominalAttributeValues[i].replaceAll("'|\"","")));
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
