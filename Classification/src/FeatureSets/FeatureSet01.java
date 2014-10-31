package FeatureSets;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.BinarySparseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Created by sudheera on 27/23/14.
 */
public class FeatureSet01 {

    Attribute segmentLength,lastWord;
    Attribute ClassAttribute;
    ArrayList featureVectorClassValues;
    ArrayList featureVectorAttributes;
    Instances TrainingSet;
    Instances TestingSet;
    Hashtable table;
    int hashval=0;

    public FeatureSet01(){

        table=new Hashtable();

        // Declare numeric attributes
        segmentLength = new Attribute("segmentLength");
        lastWord= new Attribute("lastWord");


        // Declare the class attribute along with its values
        featureVectorClassValues=new ArrayList();
        featureVectorClassValues.add("Statement");
        featureVectorClassValues.add("Request/Command/Order");
        featureVectorClassValues.add("Abandoned/Uninterpretable/Other");
        featureVectorClassValues.add("Open Question");
        featureVectorClassValues.add("Yes-No Question");
        featureVectorClassValues.add("Back-channel/Acknowledge");
        featureVectorClassValues.add("Opinion");
        featureVectorClassValues.add("Thanking");
        featureVectorClassValues.add("No Answer");
        featureVectorClassValues.add("Expressive");
        featureVectorClassValues.add("Yes Answers");
        featureVectorClassValues.add("Conventional Closing");
        featureVectorClassValues.add("Reject");
        featureVectorClassValues.add("Apology");
        featureVectorClassValues.add("Conventional Opening");
        featureVectorClassValues.add("Backchannel Question");

        ClassAttribute = new Attribute("theClass", featureVectorClassValues);

        // Declare the feature vector
        featureVectorAttributes = new ArrayList();
        featureVectorAttributes.add(segmentLength);
        featureVectorAttributes.add(lastWord);
        //class
        featureVectorAttributes.add(ClassAttribute);


        // Create an empty training set
        TrainingSet = new Instances("Rel", featureVectorAttributes,10);

        // Set class index
        TrainingSet.setClassIndex(featureVectorAttributes.size() - 1);

        // Create an empty testing set
        TestingSet = new Instances("Rel", featureVectorAttributes, 10);
        // Set class index
        TestingSet.setClassIndex(featureVectorAttributes.size() - 1);


    }

    public void initTrainingSet(String location) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(location)));
        String line;
        while ((line = br.readLine()) != null) {

            String[] split = line.split("###");

            String var = split[0].replace("?","");
            var=var.replace("!","");
            var=var.replace(".","");
            var=var.trim();
            String[] words = var.split("\\s+");

            Instance temp = new BinarySparseInstance(3);

            temp.setValue((Attribute)featureVectorAttributes.get(0),words.length);
            temp.setValue((Attribute)featureVectorAttributes.get(1),getHashValue(words[words.length-1]));

            //class value
            temp.setValue((Attribute)featureVectorAttributes.get(featureVectorAttributes.size() - 1),split[1]);

            TrainingSet.add(temp);



        }
        br.close();
    }
    public void initTestingSet(String location) throws IOException {
        BufferedReader br2 = new BufferedReader(new FileReader(new File(location)));
        String line;
        while ((line = br2.readLine()) != null) {

            String[] split = line.split("###");

            String var = split[0].replace("?","");
            var=var.replace("!","");
            var=var.replace(".","");
            var=var.trim();
            String[] words = var.split("\\s+");


            Instance temp = new BinarySparseInstance(3);
            temp.setValue((Attribute)featureVectorAttributes.get(0),words.length);
            temp.setValue((Attribute)featureVectorAttributes.get(1),getHashValue(words[words.length-1]));

            //class value
            temp.setValue((Attribute) featureVectorAttributes.get(featureVectorAttributes.size() - 1), split[1]);

            TestingSet.add(temp);


        }
        br2.close();

    }



    public void classify(String trainingFile,String testingFile) {

        try {
            initTrainingSet(trainingFile);
            initTestingSet(testingFile);


            Classifier cModel = (Classifier) new J48();
            cModel.buildClassifier(TrainingSet);

            Evaluation eTest = new Evaluation(TrainingSet);
            eTest.evaluateModel(cModel, TestingSet);


            // Print the result à la Weka explorer:
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

            System.out.println(eTest.fMeasure(0));


        } catch (Exception e) {
            e.printStackTrace();
           // System.out.println(e.getStackTrace());
        }

    }

    int getHashValue(String word){

        if(table.containsKey(word)){
            return (Integer)table.get(word);
        }else{
            table.put(word,hashval++);
            return getHashValue(word);
        }

    }


}
