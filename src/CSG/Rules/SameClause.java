package CSG.Rules;

import CSG.CSG;
import CSG.Distributions.Binary;
import CSG.Rule;
import CSG.Filter;
import Linguistic.Sentence;
import Linguistic.Utility;
import Linguistic.Word;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/21/16.
 */
public class SameClause extends Rule {

    public SameClause(Filter filter) {
        super(filter, new Binary(0.5));
    }

    @Override
    public ArrayList<CSG> apply(Sentence se) {
        ArrayList<CSG> ret = new ArrayList<CSG>();
        ArrayList<Word> cur = new ArrayList<Word>();
        for (Word w : se.words) {
            if (f.isUseful(w)) {
                if (Utility.isSentiment(w)) {
                    cur.add(w);
                } else {
                    if (cur.size() > 0) {
                        ret.add(new CSG(cur, this, d));
                        cur = new ArrayList<Word>();
                    }
                }
            }
        }
        if (cur.size() > 0) {
            ret.add(new CSG(cur, this, d));
            cur = new ArrayList<Word>();
        }
        return ret;
    }

    @Override
    public String name() {
        return "";
    }
}
