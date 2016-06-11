package Evaluation;

import Linguistic.Corpus;
import Linguistic.Sentence;
import Linguistic.Word;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by RobbinNi on 3/16/16.
 */
public class Annotator {

    public static int BAR = 5;

    private static HashMap<String, Boolean> annotation;

    private static AnnotateGUI gui;

    public static ArrayList<Pair<Sentence, Boolean>> getAnnotatedCorpus(ArrayList<String> changeList, Corpus corpus, File annot, SentimentClassifier oc, SentimentClassifier nc) throws IOException {
        annotation = new HashMap<>();
        if (annot.exists()) {
            BufferedReader bufReader = new BufferedReader(new FileReader(annot));
            String tmp;
            while ((tmp = bufReader.readLine()) != null) {
                String type = bufReader.readLine();
                annotation.put(tmp, type.equals("+"));
            }
            bufReader.close();
        } else {
            annot.createNewFile();
        }
        System.err.println("Annotation file loaded. " + annotation.size() + " annotated sentences.");
        gui = AnnotateGUI.launchGUI();
        gui.setDomain(corpus.name);
        ArrayList<Pair<Sentence, Boolean>> ret = new ArrayList<>();
        ArrayList<Sentence> total = new ArrayList<>();
        for (Sentence sen : corpus.sentences) {
            int cnt = 0;
            for (Word w : sen.words) {
                for (String s : changeList) {
                    if (s.equals(w.base)) {
                        ++cnt;
                    }
                }
            }
            if (cnt > 0) {
                if (annotation.containsKey(sen.getText().intern())) {
                    ret.add(new Pair<>(sen, annotation.get(sen.getText().intern())));
                } else {
                    total.add(sen);
                }
            }
        }
        while (ret.size() < BAR && total.size() > 0) {
            System.err.println("Total : " + total.size());
            ArrayList<Sentence> left = new ArrayList<>();
            int id = 0;
            for (Sentence sen : total) {
                ++id;
                String text = sen.getText();
                int result = gui.getAnnotation(text, ret.size(), BAR, id, total.size(), oc.classify(sen), nc.classify(sen));
                if (result == 1) {
                    annotation.put(text, true);
                    ret.add(new Pair<>(sen, true));
                } else if (result == -1) {
                    annotation.put(text, false);
                    ret.add(new Pair<>(sen, false));
                } else {
                    left.add(sen);
                }
                if (ret.size() >= BAR) {
                    break;
                }
            }
            total = left;
        }
        gui.complete(ret.size());
        BufferedWriter bufWriter = new BufferedWriter(new FileWriter(annot, false));
        for (Map.Entry<String, Boolean> entry : annotation.entrySet()) {
            bufWriter.write(entry.getKey() + "\n" + (entry.getValue() ? "+" : "-") + "\n");
        }
        bufWriter.close();
        System.err.println("Annotation complete. " + ret.size() + " annotated sentences.");
        return ret;
    }

    public static void askHowMany(ArrayList<Pair<String, Double>> list, Corpus corpus) {
        int cnt = 0;
        for (Sentence sen : corpus.sentences) {
            for (Word w : sen.words) {
                for (Pair<String, Double> s : list) {
                    if (s.getKey().equals(w.base)) {
                        ++cnt;
                        break;
                    }
                }
            }
        }
        System.out.println("# Sentences : " + cnt + "/" + corpus.sentences.size());
    }
}
