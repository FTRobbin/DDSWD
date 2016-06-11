package CSG.Rules;

import CSG.CSG;
import CSG.Filter;
import CSG.Rule;
import CSG.Distribution;
import Linguistic.Sentence;
import Linguistic.Utility;
import Linguistic.Word;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/21/16.
 */
public class ConjunctionBetween extends Rule{

    private String conj;

    public ConjunctionBetween(Filter filter, String conj, Distribution dist) {
        super(filter, dist);
        this.conj = conj;
    }

    @Override
    public ArrayList<CSG> apply(Sentence se) {
        ArrayList<CSG> ret = new ArrayList<CSG>();
        ArrayList<Word> words = new ArrayList<Word>();
        for (Word w : se.words) {
            if (f.isUseful(w) || w.base.equals(conj)) {
                words.add(w);
            }
        }
        int pos = 0;
        for (Word w : words) {
            if (w.base.equals(conj)) {
                if (pos > 0 && pos + 1 < words.size() && Utility.isSentiment(words.get(pos - 1)) && Utility.isSentiment(words.get(pos + 1))) {
                    ArrayList<Word> tmp = new ArrayList<Word>();
                    tmp.add(words.get(pos - 1));
                    tmp.add(words.get(pos + 1));
                    ret.add(new CSG(tmp, this, d));
                }
            }
            ++pos;
        }
        return ret;
    }

    @Override
    public String name() {
        return "* " + conj + " *";
    }
}
