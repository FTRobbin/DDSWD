package CSG;

import CSG.Distributions.Binary;
import CSG.Filters.ClauseFilter;
import CSG.Filters.ConjunctionFilter;
import CSG.Filters.SentimentFilter;
import CSG.Filters.SentimentFilter2;
import CSG.Rules.ConjunctionBetween;
import CSG.Rules.Neighbouring;
import CSG.Rules.SameClause;
import Linguistic.Corpus;
import Linguistic.Sentence;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/21/16.
 */
public class Miner {

    public static ArrayList<Rule> loadRules() {
        ArrayList<Rule> ret = new ArrayList<Rule>();
        //ret.add(new SameClause(new ClauseFilter()));
        ret.add(new ConjunctionBetween(new SentimentFilter2(), "and", new Binary(0.6)));
        ret.add(new ConjunctionBetween(new SentimentFilter2(), "but", new Binary(0.4)));
        ret.add(new ConjunctionBetween(new SentimentFilter2(), "however", new Binary(0.45)));
        ret.add(new ConjunctionBetween(new SentimentFilter2(), "or", new Binary(0.55)));
        ret.add(new ConjunctionBetween(new SentimentFilter2(), "although", new Binary(0.45)));
        ret.add(new Neighbouring(new SentimentFilter()));
        return ret;
    }

    public static ArrayList<CSG> mine(Corpus corpus) {
        ArrayList ret = new ArrayList<CSG>();
        ArrayList<Rule> rules = loadRules();
        for (Rule r : rules) {
            for (Sentence sen : corpus.sentences) {
                ret.addAll(r.apply(sen));
            }
        }
        return ret;
    }
}
