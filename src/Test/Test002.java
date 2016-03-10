package Test;

import CSG.CSG;
import CSG.Miner;
import CSG.Rule;
import Linguistic.Corpus;
import Linguistic.Sentence;
import Linguistic.Word;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/21/16.
 */
public class Test002 {

    private void apply(Corpus corpus, Rule rule) {
        int count = 10, pos = 0;
        while (count > 0 && pos < corpus.sentences.size()) {
            Sentence sen = corpus.sentences.get(pos++);

            ArrayList<CSG> csgs = rule.apply(sen);

            if (csgs.size() > 0) {
                System.out.println(sen.print());

                for (Word w : sen.words) {
                    if (rule.f.isUseful(w)) {
                        System.out.print(" " + w.print());
                    }
                }
                System.out.println();

                for (CSG csg : csgs) {
                    System.out.println(csg.print());
                }

                System.out.println("------------------------------------------");

                --count;
            }
        }
    }

    public void run(Corpus corpus) {
        ArrayList<Rule> rules = Miner.loadRules();
        for (Rule rule : rules) {
            apply(corpus, rule);
        }
    }
}
