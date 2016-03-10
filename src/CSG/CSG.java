package CSG;

import Linguistic.Word;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/21/16.
 */
public class CSG {

    public ArrayList<Word> sw;

    public Rule rule;

    public Distribution dist;

    public CSG(ArrayList<Word> words, Rule rule, Distribution dist) {
        sw = words;
        this.rule = rule;
        this.dist = dist;
    }

    public ArrayList<Double> getDistributionValue() {
        /*
        ArrayList<Double> vals = new ArrayList<>();
        int n = sw.size();
        for (int i = 0; i < (1 << n); ++i) {
            vals.add(1.0 / (1 << n));
        }
        */
        return dist.getDistributionValue(sw);
    }

    public String print() {
        String ret = "";
        for (Word w : sw) {
            ret += w.print() + ",";
        }
        return ret;
    }
}
