package Linguistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/6/16.
 */
public class Corpus {

    public String name;

    public ArrayList<Sentence> sentences;

    public Corpus(File input) throws IOException {
        this.name = input.getName();
        this.sentences = new ArrayList<Sentence>();
        BufferedReader bufReader = new BufferedReader(new FileReader(input));
        String tmp;
        while ((tmp = bufReader.readLine()) != null){
            sentences.add(new Sentence(tmp));
        }
        bufReader.close();
    }

    public void markSentiment(Dictionary dict) {
        for (Sentence sen : sentences) {
            for (Word w : sen.words) {
                if (dict.include(w)) {
                    w.isSentiment = true;
                }
            }
        }
    }

    public void markNegations(Dictionary nega) {
        for (Sentence sen : sentences) {
            for (Word w : sen.words) {
                if (nega.include(w)) {
                    sen.negations.add(w.pos);
                }
            }
        }
    }

    public void calcNegations() {
        for (Sentence sen : sentences) {
            for (Word w : sen.words) {
                if (Utility.isSentiment(w)) {
                    for (int it: sen.negations) {
                        if (Utility.inRange(w.pos, it)) {
                            w.negated = !w.negated;
                        }
                    }
                }
            }
        }
    }

}
