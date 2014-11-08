package FeatureSets;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.*;
import weka.classifiers.trees.lmt.LogisticBase;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Dammina on 04/11/2014.
 */
public class FeatureSetAll {
    private ArrayList<String> bow;
    private ArrayList<String> testingUtterances=new ArrayList<String>();

    private HashMap<String,Integer> Statement;
    private HashMap<String,Integer> RequestCommandOrder;
    private HashMap<String,Integer> AbandonedUninterpretableOther;
    private HashMap<String,Integer> openQuestion;
    private HashMap<String,Integer> YesNoQuestion;
    private HashMap<String,Integer> BackchannelAcknowledge;
    private HashMap<String,Integer> Opinion;
    private HashMap<String,Integer> Thanking;
    private HashMap<String,Integer> NoAnswer;
    private HashMap<String,Integer> Expressive;
    private HashMap<String,Integer> YesAnswers;
    private HashMap<String,Integer> ConventionalClosing;
    private HashMap<String,Integer> Reject;
    private HashMap<String,Integer> Apology;
    private HashMap<String,Integer> ConventionalOpening;
    private HashMap<String,Integer> BackchannelQuestion;

    Attribute segmentLength,lastWord,punctuationMark,lastletter,cuephrases, verb, previousDialogueAct, bowCount;
    Attribute ClassAttribute;
    ArrayList<String> featureVectorClassValues;
    ArrayList<Attribute> featureVectorAttributes;
    Instances TrainingSet;
    Instances TestingSet;
    Hashtable table;
    int hashval=0;
    int cueval=0;
    int previosDialogueAct= 0;

    String verbslist="හොයන්න, හැරෙනවා, ඉන්නකෝ, යොදාගන්න, නඟිනවා, නමපන්, ජීවත්වෙන්න, එනවා, උගන්නන්න, ඉඩතියන්න, අදින්න, දියල්ලා, ගන්න, දකින්න, දෙන්න, බැහැපං, හිතන්, ඇවිදින්න, හිටහන්, කරන්න පටංගත්තොත්, නැවතියන්, නැඟිටින්න, ඉන්න, දීපන්, එන්න, නැගිටිනවා, කියපං, වරෙල්ලා, පලයං, එහෙනම් ඔයා මාව අස්කරලා දාන්න, දුවපන්, පැදපන්, සංතෝෂවෙයල්ලා, කතාබහකරන්න, ඇඳගන්න, ගොඩඑන්න, දුවපල්ලා, අල්ලන්න, පෙරළන්න, ඇරපන්, බලාගන්න, එවන්න, නවත්වනවා, දීපල්ලා, ගේන්න, බලමු, නගින්න, බහින්න, අස්කරගන්න, කරනවා, දෙනවා, නවත්තන්න, දුක්වෙන්න, යන්නකෝ, මරන්න, යමල්ලා, යවන්න, ඔබන්න, හිටගන්නවා, කියන්නකෝ, තේරුම්ගන්න, කාපන්, වහගන්නවා, අරගන්න, වෙනවා, යමන්, එන්න ඇතුලට, කරන්න, වරෙන්,, තියන්න, නිදාගන්න, ඉන්නවා, ගේ්න්න, ගැටගහන්න, කතාකරන්න, කියපන්, කරගන්න,ලිහන්න, මනින්න, වෙන්න, රවට්ටන්න, අඬන්න, හිටපං, දාපන්, අහන්න, හිනාවෙන්න, අරිනවා, වාඩිවෙන්න, උස්සන්න, දුවන්න, හරිගස්සන්න, ගහගන්න, වෙනවා,වදවෙන්න, අරින්න, නගිනවා, නැගිට්ටවන්න, පලයල්ලා, ගෙනියන්න, වෙයල්ලා, නිදාගනින්, වාඩිවෙන්න,වෙන්න, වක්කරපන්, බේරගන්න, වරෙන්, දියන්, වදවෙන්න, බයවෙන්න, පලයන්, පුහුණුවෙන්න, ඉවසන්න, ගහන්න, ඉදපන්, හිටපන්, නැඟිටපල්ලා, දාන්න, හිටපංකෝ, බලන්න, කන්න, ගනින්, ඉඩදෙන්න, පෙන්වන්න, කරහන්, අහන්නකෝ, මැරියන්, තියාගන්න, පටන්ගන්න, දාගන්න, උඩින් තියන්න, කියන්න, යන්න, නවතින්න, නඟින්න, එකතුවෙන්න, කරපන්, එන්නකෝ, යනවා, අතදාන්න, කරගන්න, අල්ලගන්න, වෙයන්, හිතන්න, විවේකගනින්, වරෙව්, නැඟිටපන්, කඩන්න";
    List<String> verbs;

    public FeatureSetAll(){

        table=new Hashtable<String,Integer>();

        // Declare numeric attributes
        segmentLength = new Attribute("segmentLength");
        lastWord = new Attribute("lastWord");
        punctuationMark = new Attribute("punctuation");
        lastletter = new Attribute("lastletter");
        cuephrases = new Attribute("cuephrases");
        verb = new Attribute("verb");
        previousDialogueAct = new Attribute("previousDialogueAct");
        bowCount = new Attribute("bowCount");

        //predefined verbs
        verbs= Arrays.asList(verbslist.split(", "));


        // Declare the class attribute along with its values
        featureVectorClassValues=new ArrayList<String>();
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
        featureVectorAttributes = new ArrayList<Attribute>();
        featureVectorAttributes.add(segmentLength);
        featureVectorAttributes.add(lastWord);
        featureVectorAttributes.add(punctuationMark);
        featureVectorAttributes.add(lastletter);
        featureVectorAttributes.add(cuephrases);
        featureVectorAttributes.add(verb);
//        featureVectorAttributes.add(previousDialogueAct);
//        featureVectorAttributes.add(bowCount);
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
            char lastletter = words[words.length-1].charAt(words[words.length-1].length()-1);

            //cue phrases feature value declaration
            List wordlist = Arrays.asList(words);

            if(wordlist.contains("ඇත්තෙන්ම")){
                cueval+=1;
            }
            if(wordlist.contains("සහ") || wordlist.contains("හා")){
                cueval+=2;
            }
            if(wordlist.contains("නිසා") || wordlist.contains("හින්ද")){
                cueval+=4;
            }
            if(wordlist.contains("එසේම")){
                cueval+=8;
            }
            if(wordlist.contains("එභෙත්") || wordlist.contains("නමුත්")){
                cueval+=16;
            }
            if(wordlist.contains("වගේ") || wordlist.contains("වැනි") || wordlist.contains("වාගේ")){
                cueval+=32;
            }
            if(wordlist.contains("ඉතින්") || wordlist.contains("එවිට")){
                cueval+=64;
            }
            if(wordlist.contains("හෝ")){
                cueval+=128;
            }
            if(wordlist.contains("හරි")){
                cueval+=256;
            }
            if(wordlist.contains("එනිසා") || wordlist.contains("එබැවින්")){
                cueval+=512;
            }

            Instance temp = new DenseInstance(7);

            temp.setValue(featureVectorAttributes.get(0),words.length);
            temp.setValue(featureVectorAttributes.get(1),getHashValue(words[words.length-1]));
            temp.setValue(featureVectorAttributes.get(2),getPunctuationMark(split[0]));
            if(lastletter=='ද'){
                temp.setValue(featureVectorAttributes.get(3),1);
            }
            else{
                temp.setValue(featureVectorAttributes.get(3),0);
            }
            temp.setValue(featureVectorAttributes.get(4),cueval);
            cueval=0;

            //verbs
            boolean local_flag=false;
            for(int i=words.length-1;i>=0;i--){
                if(verbs.contains(words[i])){
                    temp.setValue(featureVectorAttributes.get(5),1);
                    local_flag=true;
                    break;
                }
            }
            if(!local_flag){
                temp.setValue(featureVectorAttributes.get(5),0);
            }

//            //update previous dialogue act value
//            if(split[1].equalsIgnoreCase("Open Question")){
//                previosDialogueAct = 1;
//            }
//            else if(split[1].equalsIgnoreCase("Yes-No Question")){
//                previosDialogueAct = 2;
//            }
//            else{
//                previosDialogueAct = 0;
//            }
//            if(previosDialogueAct!=0)
//                setPreviosDialogueAct(previosDialogueAct);
//            temp.setValue(featureVectorAttributes.get(6),previosDialogueAct);
//            temp.setValue(featureVectorAttributes.get(7),getBoWValue(words));

            //class value
            temp.setValue(featureVectorAttributes.get(featureVectorAttributes.size() - 1),split[1]);
            temp.setDataset(TrainingSet);
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
            char lastletter = words[words.length-1].charAt(words[words.length-1].length()-1);
            List wordlist = Arrays.asList(words);

            if(wordlist.contains("ඇත්තෙන්ම")){
                cueval+=1;
            }
            if(wordlist.contains("සහ") || wordlist.contains("හා")){
                cueval+=2;
            }
            if(wordlist.contains("නිසා") || wordlist.contains("හින්ද")){
                cueval+=4;
            }
            if(wordlist.contains("එසේම")){
                cueval+=8;
            }
            if(wordlist.contains("එභෙත්") || wordlist.contains("නමුත්")){
                cueval+=16;
            }
            if(wordlist.contains("වගේ") || wordlist.contains("වැනි") || wordlist.contains("වාගේ")){
                cueval+=32;
            }
            if(wordlist.contains("ඉතින්") || wordlist.contains("එවිට")){
                cueval+=64;
            }
            if(wordlist.contains("හෝ")){
                cueval+=128;
            }
            if(wordlist.contains("හරි")){
                cueval+=256;
            }
            if(wordlist.contains("එනිසා") || wordlist.contains("එබැවින්")){
                cueval+=512;
            }
            Instance temp = new DenseInstance(7);

            temp.setValue(featureVectorAttributes.get(0),words.length);
            temp.setValue(featureVectorAttributes.get(1),getHashValue(words[words.length-1]));
            temp.setValue(featureVectorAttributes.get(2),getPunctuationMark(split[0]));
            if(lastletter=='ද'){
                temp.setValue(featureVectorAttributes.get(3),1);
            }
            else{
                temp.setValue(featureVectorAttributes.get(3),0);
            }
            temp.setValue(featureVectorAttributes.get(4),cueval);
            cueval=0;

            //verbs
            boolean local_flag=false;
            for(int i=words.length-1;i>=0;i--){
                if(verbs.contains(words[i])){
                    temp.setValue(featureVectorAttributes.get(5),1);
                    local_flag=true;
                    break;
                }
            }
            if(!local_flag){
                temp.setValue(featureVectorAttributes.get(5),0);
            }

//            //update previous dialogue act value
//            if(split[1].equalsIgnoreCase("Open Question")){
//                previosDialogueAct = 1;
//            }
//            else if(split[1].equalsIgnoreCase("Yes-No Question")){
//                previosDialogueAct = 2;
//            }
//            else{
//                previosDialogueAct = 0;
//            }
//            if(previosDialogueAct!=0)
//                setPreviosDialogueAct(previosDialogueAct);
//            temp.setValue(featureVectorAttributes.get(6),previosDialogueAct);
//            temp.setValue(featureVectorAttributes.get(7),getBoWValue(words));

            //class value
            temp.setValue(featureVectorAttributes.get(featureVectorAttributes.size() - 1), split[1]);
            temp.setDataset(TestingSet);
            TestingSet.add(temp);


        }
        br2.close();

    }



    public void classify(String trainingFile,String testingFile) {

        try {

            initiateBagOfWords(trainingFile);
            initTrainingSet(trainingFile);

            initiateBagOfWords(testingFile);
            initTestingSet(testingFile);


            Classifier cModel = (Classifier)new J48();
            //cModel.setUnpruned(true);
            cModel.buildClassifier(TrainingSet);

//            for (int i = 0; i < TestingSet.numInstances(); i++) {
//                double pred = cModel.classifyInstance(TestingSet.instance(i));
//                if (!testingUtterances.get(i).contains(TestingSet.classAttribute().value((int) pred))){
//                    System.out.print(testingUtterances.get(i));
//                    //System.out.print(", actual: " + TestingSet.classAttribute().value((int) TestingSet.instance(i).classValue()));
//                    System.out.println(", predicted: " + TestingSet.classAttribute().value((int) pred));
//                }
//            }

            Evaluation eTest = new Evaluation(TrainingSet);
            eTest.evaluateModel(cModel, TestingSet);


            //print out the results
            System.out.println("=====================================================================");
            System.out.println("Results for "+this.getClass().getSimpleName());
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

            InfoGainAttributeEval infoGainAttributeEval = new InfoGainAttributeEval();
            infoGainAttributeEval.buildEvaluator(TrainingSet);

            System.out.println("segmentLength : "+infoGainAttributeEval.evaluateAttribute(0));
            System.out.println("lastWord : "+infoGainAttributeEval.evaluateAttribute(1));
            System.out.println("punctuationMark : "+infoGainAttributeEval.evaluateAttribute(2));
            System.out.println("lastletter : "+infoGainAttributeEval.evaluateAttribute(3));
            System.out.println("cuephrases : "+infoGainAttributeEval.evaluateAttribute(4));
            System.out.println("verb : "+infoGainAttributeEval.evaluateAttribute(5));
//            System.out.println("previousUtterance : "+infoGainAttributeEval.evaluateAttribute(6));
//            System.out.println("bow : "+infoGainAttributeEval.evaluateAttribute(7));

            System.out.println("=====================================================================");

            System.out.println("recall : "+eTest.weightedRecall());
            System.out.println("precision : "+eTest.weightedPrecision());
            System.out.println("F-measure : "+eTest.weightedFMeasure());

            System.out.println("================= Rounded Values =========================");

            System.out.println("recall : "+Math.round(eTest.weightedRecall() * 100.0) / 100.0);
            System.out.println("precision : "+Math.round(eTest.weightedPrecision() * 100.0) / 100.0);
            System.out.println("F-measure : "+Math.round(eTest.weightedFMeasure() * 100.0) / 100.0);
            System.out.println("=====================================================================");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getHashValue(String word){

        if(table.containsKey(word)){
            return (Integer)table.get(word);
        }else{
            table.put(word,hashval++);
            return getHashValue(word);
        }

    }


    private int getPunctuationMark(String line){

        if(line.contains("?") && !line.contains("!")){
            return 5;
        }
        else if(line.contains("?") && line.contains("!")){
            return 4;
        }else if(line.contains("!")){
            return 3;
        }else if(line.contains(".")){
            return 2;
        }else if(line.contains("\"")||line.contains("\'")||line.contains("`")){
            return 1;
        }else
            return 0;
    }
    private int getPreviosDialogueAct(){
        return previosDialogueAct;
    }
    private void setPreviosDialogueAct(int pda){
        previosDialogueAct=pda;
    }
    private void initiateBagOfWords(String location) throws IOException {
        bow = new ArrayList<String>();
        Statement = new HashMap<String,Integer>();
        RequestCommandOrder = new HashMap<String,Integer>();
        AbandonedUninterpretableOther = new HashMap<String,Integer>();
        openQuestion = new HashMap<String,Integer>();
        YesNoQuestion = new HashMap<String,Integer>();
        BackchannelAcknowledge = new HashMap<String,Integer>();
        Opinion = new HashMap<String,Integer>();
        Thanking = new HashMap<String,Integer>();
        NoAnswer = new HashMap<String,Integer>();
        Expressive = new HashMap<String,Integer>();
        YesAnswers = new HashMap<String,Integer>();
        ConventionalClosing = new HashMap<String,Integer>();
        Reject = new HashMap<String,Integer>();
        Apology = new HashMap<String,Integer>();
        ConventionalOpening = new HashMap<String,Integer>();
        BackchannelQuestion = new HashMap<String,Integer>();


        BufferedReader br = new BufferedReader(new FileReader(new File(location)));
        String line;
        while ((line = br.readLine()) != null) {

            String[] split = line.split("###");

            String var = split[0].replace("?","");
            var=var.replace("!","");
            var=var.replace(".","");
            var=var.trim();
            String[] wds = var.split("\\s+");
            String[] words ={wds[wds.length-1]};


            if(split[1].equalsIgnoreCase("Open Question")){
                for (int i = 0; i < words.length ; i++) {
                    if(openQuestion.containsKey(words[i])){
                        int count = openQuestion.get(words[i]);
                        count++;
                        openQuestion.put(words[i],count);

                    }else{
                        openQuestion.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Statement")){
                for (int i = 0; i < words.length ; i++) {
                    if(Statement.containsKey(words[i])){
                        int count = Statement.get(words[i]);
                        count++;
                        Statement.put(words[i],count);

                    }else{
                        Statement.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Request/Command/Order")){
                for (int i = 0; i < words.length ; i++) {
                    if(RequestCommandOrder.containsKey(words[i])){
                        int count = RequestCommandOrder.get(words[i]);
                        count++;
                        RequestCommandOrder.put(words[i],count);

                    }else{
                        RequestCommandOrder.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Abandoned/Uninterpretable/Other")){
                for (int i = 0; i < words.length ; i++) {
                    if(AbandonedUninterpretableOther.containsKey(words[i])){
                        int count = AbandonedUninterpretableOther.get(words[i]);
                        count++;
                        AbandonedUninterpretableOther.put(words[i],count);

                    }else{
                        AbandonedUninterpretableOther.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Yes-No Question")){
                for (int i = 0; i < words.length ; i++) {
                    if(YesNoQuestion.containsKey(words[i])){
                        int count = YesNoQuestion.get(words[i]);
                        count++;
                        YesNoQuestion.put(words[i],count);

                    }else{
                        YesNoQuestion.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Back-channel/Acknowledge")){
                for (int i = 0; i < words.length ; i++) {
                    if(BackchannelAcknowledge.containsKey(words[i])){
                        int count = BackchannelAcknowledge.get(words[i]);
                        count++;
                        BackchannelAcknowledge.put(words[i],count);

                    }else{
                        BackchannelAcknowledge.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Opinion")){
                for (int i = 0; i < words.length ; i++) {
                    if(Opinion.containsKey(words[i])){
                        int count = Opinion.get(words[i]);
                        count++;
                        Opinion.put(words[i],count);

                    }else{
                        Opinion.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Thanking")){
                for (int i = 0; i < words.length ; i++) {
                    if(Thanking.containsKey(words[i])){
                        int count = Thanking.get(words[i]);
                        count++;
                        Thanking.put(words[i],count);

                    }else{
                        Thanking.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("No Answer")){
                for (int i = 0; i < words.length ; i++) {
                    if(NoAnswer.containsKey(words[i])){
                        int count = NoAnswer.get(words[i]);
                        count++;
                        NoAnswer.put(words[i],count);

                    }else{
                        NoAnswer.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Expressive")){
                for (int i = 0; i < words.length ; i++) {
                    if(Expressive.containsKey(words[i])){
                        int count = Expressive.get(words[i]);
                        count++;
                        Expressive.put(words[i],count);

                    }else{
                        Expressive.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Yes Answers")){
                for (int i = 0; i < words.length ; i++) {
                    if(YesAnswers.containsKey(words[i])){
                        int count = YesAnswers.get(words[i]);
                        count++;
                        YesAnswers.put(words[i],count);

                    }else{
                        YesAnswers.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Conventional Closing")){
                for (int i = 0; i < words.length ; i++) {
                    if(ConventionalClosing.containsKey(words[i])){
                        int count = ConventionalClosing.get(words[i]);
                        count++;
                        ConventionalClosing.put(words[i],count);

                    }else{
                        ConventionalClosing.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Reject")){
                for (int i = 0; i < words.length ; i++) {
                    if(Reject.containsKey(words[i])){
                        int count = Reject.get(words[i]);
                        count++;
                        Reject.put(words[i],count);

                    }else{
                        Reject.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Apology")){
                for (int i = 0; i < words.length ; i++) {
                    if(Apology.containsKey(words[i])){
                        int count = Apology.get(words[i]);
                        count++;
                        Apology.put(words[i],count);

                    }else{
                        Apology.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Conventional Opening")){
                for (int i = 0; i < words.length ; i++) {
                    if(ConventionalOpening.containsKey(words[i])){
                        int count = ConventionalOpening.get(words[i]);
                        count++;
                        ConventionalOpening.put(words[i],count);

                    }else{
                        ConventionalOpening.put(words[i],1);
                    }

                }
            }else if(split[1].equalsIgnoreCase("Backchannel Question")){
                for (int i = 0; i < words.length ; i++) {
                    if(BackchannelQuestion.containsKey(words[i])){
                        int count = BackchannelQuestion.get(words[i]);
                        count++;
                        BackchannelQuestion.put(words[i],count);

                    }else{
                        BackchannelQuestion.put(words[i],1);
                    }

                }
            }

        }

        int[] maxVals;
        String[] maxKeys;

        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Statement.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : RequestCommandOrder.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);

        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : AbandonedUninterpretableOther.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : openQuestion.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : YesNoQuestion.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : BackchannelAcknowledge.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Opinion.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Thanking.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : NoAnswer.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Expressive.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : YesAnswers.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : ConventionalClosing.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Reject.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : Apology.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : ConventionalOpening.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);


        maxVals=new int[3];
        maxKeys=new String[3];
        maxVals[0]=Integer.MIN_VALUE;
        maxVals[1]=Integer.MIN_VALUE;
        maxVals[2]=Integer.MIN_VALUE;
        for(Map.Entry<String,Integer> entry : BackchannelQuestion.entrySet()) {
            if(entry.getValue() > maxVals[0]) {

                maxVals[2] = maxVals[1];
                maxKeys[2] = maxKeys[1];

                maxVals[1] = maxVals[0];
                maxKeys[1] = maxKeys[0];

                maxVals[0] = entry.getValue();
                maxKeys[0] = entry.getKey();
            }
        }
        bow.add(maxKeys[0]);
        bow.add(maxKeys[1]);
//        bow.add(maxKeys[2]);

    }

    private int getBoWValue(String[] wds) {

        String word = wds[wds.length-1];
        int tag_id;
        if(bow.contains(word)){
            tag_id=bow.indexOf(word);
            if(bow.indexOf(word)!=bow.lastIndexOf(word)){
                tag_id=-1;
            }
            tag_id=(int)Math.floor(tag_id/2.0)+1;
        }else{
            tag_id=0;
        }

        return tag_id;




    }
}
