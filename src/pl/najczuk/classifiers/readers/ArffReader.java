package pl.najczuk.classifiers.readers;

import com.sun.deploy.util.StringUtils;
import pl.najczuk.classifiers.instances.Attribute;
import pl.najczuk.classifiers.instances.Instances;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Adrian
 * Date: 10/31/2014
 * Time: 21:07
 */
public class ArffReader {
    private FileReader fileReader;
    private Instances instances;
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileReader getFileReader() throws FileNotFoundException {
        return new FileReader(getFilePath());
    }


    public Instances getInstances() {
        return instances;
    }

    public void setInstances(Instances instances) {
        this.instances = instances;
    }

    public ArffReader(String filePath) {
        setFilePath(filePath);
        setInstances(extractInstancesFromFile());
        System.out.println(getInstances());
    }


    private Instances extractInstancesFromFile() {
        ArrayList<Attribute> attributes = extractAttributesFromFile();
        ArrayList<ArrayList<String>> instancesData = extractInstancesDataFromFile(attributes);
        Instances instances = new Instances(attributes, instancesData);


        return instances;

    }

    private ArrayList<ArrayList<String>> extractInstancesDataFromFile(ArrayList<Attribute> attributes) {
        Scanner in = null;
        String line;
        try {
            in = new Scanner(getFileReader());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Pattern dataTagPattern = Pattern.compile("^@data", Pattern.CASE_INSENSITIVE);
        Matcher dataTagMatcher;
        ArrayList<ArrayList<String>> instancesData = new ArrayList<ArrayList<String>>();
        boolean fileCursorBelowDataTag = false;
        while (in.hasNextLine()) {
            line = in.nextLine();
            dataTagMatcher = dataTagPattern.matcher(line);
            if (dataTagMatcher.matches()) {
                fileCursorBelowDataTag = true;
                continue;
            }
            if (fileCursorBelowDataTag) {
                String[] instanceData = line.split(",");
                instancesData.add(new ArrayList<String>(Arrays.asList(instanceData)));
            }
        }
        return instancesData;
    }

    private ArrayList<Attribute> extractAttributesFromFile() {
        Scanner in = null;
        try {
            in = new Scanner(getFileReader());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();

        String line;
        Matcher nominalAttributeValuesMatcher, attributeNameMatcher, numericAttributeMatcher,
                nominalAttributeMatcher, dataTagMatcher;
        Pattern numericAttributePattern = Pattern.compile("^@attribute\\s+.*\\s+real", Pattern.CASE_INSENSITIVE);
        Pattern nominalAttributePattern = Pattern.compile("^@attribute\\s+.*\\s+\\{.*\\}", Pattern.CASE_INSENSITIVE);
        Pattern nominalAttributeValuesPattern = Pattern.compile("\\{(.*?)\\}");
        Pattern attributeNamePattern = Pattern.compile("^@attribute\\s+(.*?)\\s+", Pattern.CASE_INSENSITIVE);
        Pattern dataTagPattern = Pattern.compile("^@data\\s+", Pattern.CASE_INSENSITIVE);

        while (in.hasNextLine()) {
            line = in.nextLine();
            numericAttributeMatcher = numericAttributePattern.matcher(line);
            nominalAttributeMatcher = nominalAttributePattern.matcher(line);
            attributeNameMatcher = attributeNamePattern.matcher(line);
            dataTagMatcher = dataTagPattern.matcher(line);

            if (attributeNameMatcher.find()) {
                String attributeName;
                attributeName = attributeNameMatcher.group(1);

                if (numericAttributeMatcher.matches()) {
                    attributes.add(new Attribute(attributeName));

                } else if (nominalAttributeMatcher.matches()) {
                    nominalAttributeValuesMatcher = nominalAttributeValuesPattern.matcher(line);
                    if (nominalAttributeValuesMatcher.find()) {

                        String nominalAttributeValuesDelimited = nominalAttributeValuesMatcher.group(1);
                        String[] nominalAttributeValues = nominalAttributeValuesDelimited.split(",");
                        for (int i = 0; i < nominalAttributeValues.length; i++) {
                            nominalAttributeValues[i] = StringUtils.trimWhitespace(nominalAttributeValues[i]);
                        }
                        attributes.add(new Attribute(attributeName, new ArrayList<String>(Arrays.asList
                                (nominalAttributeValues))));
                    }
                }
            } else if (dataTagMatcher.matches()) {
                break;
            }
        }
        return attributes;
    }


}
