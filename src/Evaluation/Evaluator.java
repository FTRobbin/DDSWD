package Evaluation;

import Linguistic.Sentence;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 3/16/16.
 */
public class Evaluator {

    public static int evaluate(ArrayList<Pair<Sentence, Boolean> > list, SentimentClassifier classifier) {
        int currect = 0;
        for (Pair<Sentence, Boolean> p : list) {
            if (classifier.classify(p.getKey()) == p.getValue()) {
                ++currect;
            } else {
                System.out.println(p.getKey().getText() + (p.getValue() ? " +" : " -"));
            }
        }
        return currect;
    }

    public static ArrayList<Integer> evaluate(ArrayList<Pair<Sentence, Boolean>> list, ArrayList<SentimentClassifier> classifiers) {
        ArrayList<Integer> ret = new ArrayList<>();
        for (SentimentClassifier classifier : classifiers) {
            ret.add(evaluate(list, classifier));
        }
        return ret;
    }
}
