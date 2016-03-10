package Main;

import BP.BeliefPropagation;
import BP.ClusterGraph;
import CSG.CSG;
import CSG.Miner;
import Classification.Classifier;
import Classification.Report;
import Linguistic.Corpus;
import Linguistic.Dictionary;
import Linguistic.Utility;
import Test.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

        printReport();
    }
}
