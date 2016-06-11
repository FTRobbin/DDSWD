package Linguistic;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/6/16.
 */
public class Sentence {

    public ArrayList<Word> words;

    public ArrayList<Integer> negations;

    public Sentence(String line) {
        words = new ArrayList<Word>();
        negations = new ArrayList<Integer>();
        line.trim();
        String[] tokens = line.split(" ");
        int pos = 0;
        for (String token : tokens) {
            words.add(new Word(token, pos++));
        }
    }

    public String print() {
        String ret = "";
        for (Word w : words) {
            ret = ret + (" " + w.print());
        }
        return ret;
    }

    public String getText() {
        String ret = "";
        for (Word w : words) {
            ret = ret + (" " + w.ori);
        }
        return ret;
    }
}
