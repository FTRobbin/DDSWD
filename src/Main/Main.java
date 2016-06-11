package Main;

import BP.BeliefPropagation;
import BP.ClusterGraph;
import CSG.CSG;
import CSG.Miner;
import Classification.Classifier;
import Classification.Report;
import Evaluation.Annotator;
import Evaluation.Evaluator;
import Evaluation.SentimentClassifier;
import Linguistic.Corpus;
import Linguistic.Dictionary;
import Linguistic.Sentence;
import Linguistic.Utility;
import Test.*;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.exit;

/**
 * Created by RobbinNi on 2/6/16.
 */
public class Main {

    static private Dictionary positive, negative, negation;

    static private Corpus corpus;

    static private ArrayList<CSG> CSGs;

    static private ClusterGraph graph;

    static private ArrayList<Pair<String, Double>> dist;

    static private Report report;

    static void loadDict() {
        String posiPath = "res/dict/positive-words.txt";
        String negaPath = "res/dict/negative-words.txt";
        String notPath = "res/dict/negation-words.txt";
        try {
            positive = new Dictionary(new File(posiPath));
            negative = new Dictionary(new File(negaPath));
            negation = new Dictionary(new File(notPath));
        } catch (IOException ie) {
            ie.printStackTrace();
            exit(1);
        }
        Utility.positive = positive;
        Utility.negative = negative;
        System.err.println("Dictionaries loaded.");
    }

    static void loadCorpus(String args[]) {
        String corPath = "res/corpus/debug.txt",
               name = "debug";
        if (args.length > 0) {
            corPath = "res/corpus/" + args[0] + ".txt";
            name = args[0];
        }
        try {
            corpus = new Corpus(new File(corPath));
        } catch (IOException ie) {
            ie.printStackTrace();
            exit(1);
        }
        System.err.println("Corpus " + name + " loaded. " + corpus.sentences.size() + " sentences.");
    }

    static void markSentiments() {
        corpus.markSentiment(positive);
        corpus.markSentiment(negative);
    }

    static void markNegations() {
        corpus.markNegations(negation);
    }

    static void mineCSGs() {
        CSGs = Miner.mine(corpus);
        System.err.println("CSG mined. " + CSGs.size() + " CSGs.");
    }

    static void handleNegations() {
        corpus.calcNegations();
    }

    static void buildGraph() {
        graph = BeliefPropagation.buildGraph(CSGs);
        System.err.println("Graph built. " + graph.nodes.size() + " nodes, " + graph.edges.size() + " edges.");
    }

    static void beliefPropagation() {
        BeliefPropagation.beliefPropagation(graph);
    }

    static void calcDistribution() {
        dist = Classifier.calcDistribution(graph);
    }

    static void generateReport() {
        ArrayList<Dictionary> dicts = new ArrayList<>();
        dicts.add(positive);
        dicts.add(negative);
        dicts.add(negation);
        report = new Report(dicts, corpus, CSGs, graph, dist);
    }

    static void printReport() {
        System.out.println(report.print());
        File log = new File("log" + currentTimeMillis() + ".txt");
        try {
            OutputStream output = new FileOutputStream(log);
            output.write(report.print().getBytes());
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    static ArrayList<SentimentClassifier> getSentimentClassifiers(ArrayList<Pair<String, Double>> changeList) {
        ArrayList<SentimentClassifier> ret = new ArrayList<>();
        HashSet<String> correct = new HashSet<>();
        try {
            String path = "res/corpus/" + corpus.name.replace(".txt", "_correct.txt");
            BufferedReader bufReader = new BufferedReader(new FileReader(new File(path)));
            String tmp;
            while ((tmp = bufReader.readLine()) != null) {
                correct.add(tmp);
            }
            bufReader.close();
        } catch (IOException ie) {
            ie.printStackTrace();
            System.exit(1);
        }
        ret.add(SentimentClassifier.getClassifier(null, positive, negative));
        ArrayList<Pair<String, Double>> cur = new ArrayList<>(), cor = new ArrayList<>();
        for (int i = 0; i < changeList.size(); ++i) {
            cur.add(changeList.get(i));
            if (correct.contains(changeList.get(i).getKey().intern())) {
                cor.add(changeList.get(i));
            }
            if (cur.size() == 5 || cur.size() == 10 || cur.size() == 20) {
                ret.add(SentimentClassifier.getClassifier(cur, positive, negative));
                Annotator.askHowMany(cur, corpus);
                //ret.add(SentimentClassifier.getClassifier(cor, positive, negative));
            }
        }
        cur.clear();
        try {
            String path = "res/baseline/report_" + corpus.name;
            BufferedReader bufReader = new BufferedReader(new FileReader(new File(path)));
            String tmp;
            while ((tmp = bufReader.readLine()) != null) {
                String word = tmp.split(" ")[0];
                Double dir = positive.include(word) ? Utility.NEGABAR - 0.01 : Utility.POSIBAR + 0.01;
                cur.add(new Pair<>(word, dir));
                if (cur.size() == 5 || cur.size() == 10 || cur.size() == 20) {
                    ret.add(SentimentClassifier.getClassifier(cur, positive, negative));
                }
            }
            bufReader.close();
        } catch (IOException ie) {
            ie.printStackTrace();
            System.exit(1);
        }
        //ret.add(SentimentClassifier.getClassifier(cur, positive, negative));
        return ret;
    }

    static public void evaluation(String args[]) {
        Annotator.BAR = Integer.valueOf(args[1]);
        ArrayList<Pair<String, Double>> changeList = report.getChangeList();
        ArrayList<String> plainList = new ArrayList<>();
        for (Pair<String, Double> p : changeList) {
            plainList.add(p.getKey());
        }
        System.err.println("Change list calculated. " + changeList.size() + " words.");
        SentimentClassifier oldClassifier = SentimentClassifier.getClassifier(null, positive, negative),
                newClassifier = SentimentClassifier.getClassifier(changeList, positive, negative);
        String annotPath = "res/corpus/" + corpus.name.replace(".txt", "") + "_annotation.txt";
        ArrayList<Pair<Sentence, Boolean>> data = null;
        try {
            data = Annotator.getAnnotatedCorpus(plainList, corpus, new File(annotPath), oldClassifier, newClassifier);
        } catch (IOException ie) {
            ie.printStackTrace();
            exit(1);
        }
        System.err.println("Annotation complete. " + data.size());
        ArrayList<SentimentClassifier> classifiers = getSentimentClassifiers(changeList);
        ArrayList<Integer> ret = Evaluator.evaluate(data, classifiers);
        report.addEvaluation(changeList, data, ret);
    }

    static public void main(String args[]) {
        //(new Test001()).run();
        loadDict();
        loadCorpus(args);

        markSentiments();
        markNegations();
        //(new Test002()).run(corpus);
        handleNegations();
        mineCSGs();

        buildGraph();
        //(new Test003()).run(graph);
        beliefPropagation();
        //(new Test004()).run(graph, positive, negative);

        calcDistribution();
        generateReport();

        evaluation(args);

        printReport();
    }

}
