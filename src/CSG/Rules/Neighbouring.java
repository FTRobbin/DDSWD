package CSG.Rules;

import CSG.CSG;
import CSG.Distributions.Binary;
import CSG.Rule;
import CSG.Filter;
import Linguistic.Sentence;
import Linguistic.Word;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/22/16.
 */
public class Neighbouring extends Rule {

    public Neighbouring(Filter filter) {
        super(filter, new Binary(0.55));
    }

    @Override
    public ArrayList<CSG> apply(Sentence se) {
        ArrayList<CSG> ret = new ArrayList<CSG>();
        Word last = null;
        for (Word w : se.words) {
            if (f.isUseful(w)) {
                if (last != null) {
                    ArrayList<Word> tmp = new ArrayList<>();
                    tmp.add(last);
                    tmp.add(w);
                    ret.add(new CSG(tmp, this, d));
                }
                last = w;
            }
        }
        return ret;
    }

    @Override
    public String name() {
        return "* *";
    }
}
