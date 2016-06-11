package Linguistic;

/**
 * Created by RobbinNi on 2/21/16.
 */
final public class Utility {

    static public Dictionary positive, negative;

    static public Double POSIBAR = 0.6, NEGABAR = 0.3;

    static public boolean isSentiment(Word w) {
        return w.isSentiment
                || w.tag.equals(POS.JJ)
                || w.tag.equals(POS.JJR)
                || w.tag.equals(POS.JJS);
        /*
                || w.tag.equals(POS.RB)
                || w.tag.equals(POS.RBR)
                || w.tag.equals(POS.RBS);
        */
    }

    static public boolean inRange(int i, int j) {
        return j >= i - 4 && j <= i + 2;
    }

    static public double getOriginalValue(String s) {
        if (positive.include(s)) {
            return 0.6;
        } else if (negative.include(s)) {
            return 0.3;
        } else {
            return 0.5;
        }
    }
}
