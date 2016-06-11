package Evaluation;

import Linguistic.Dictionary;
import Linguistic.Sentence;
import Linguistic.Utility;
import Linguistic.Word;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 3/16/16.
 */
public class SentimentClassifier {

    private Dictionary positive, negative;

    private SentimentClassifier(Dictionary posi, Dictionary nega) {
        this.positive = posi;
        this.negative = nega;
    }

    public boolean classify(Sentence sen) {
        double score = 0;
        for (Word w : sen.words) {
            double delta = 0;
            if (positive.include(w)) {
                delta = 1;
            } else if (negative.include(w)) {
                delta = -1;
            }
            if (w.negated) {
                delta = -delta;
            }
            delta *= w.intense;
            score += delta;
        }
        return score > 0;
    }

    static public SentimentClassifier getClassifier(ArrayList<Pair<String, Double> > changeList, Dictionary positive, Dictionary negative) {
        Dictionary nposi = new Dictionary(positive), nnega = new Dictionary(negative);
        if (changeList != null) {
            for (Pair<String, Double> p : changeList) {
                String s = p.getKey();
                Double v = p.getValue();
                nposi.removeIfFound(s);
                nnega.removeIfFound(s);
                if (v >= Utility.POSIBAR) {
                    System.out.println(s + " + ");
                    nposi.addIfNotFound(s);
                }
                if (v <= Utility.NEGABAR) {
                    System.out.println(s + " - ");
                    nnega.addIfNotFound(s);
                }
                if (v > Utility.NEGABAR && v < Utility.POSIBAR) {
                    System.out.println(s + " o ");
                }
            }
        }
        return new SentimentClassifier(nposi, nnega);
    }
}
