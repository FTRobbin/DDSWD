package Linguistic;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by RobbinNi on 2/6/16.
 */
public class Word {

    public String base;

    public POS tag;

    public int pos;

    public boolean isSentiment;

    public boolean negated;

    public Word(String token, int position) {
        token = token.replace("\\/", "\\");
        String[] tmp = token.split("/");
        if (tmp.length < 3) {
            System.out.println(token);
            base = "";
            tag = POS.OTHER;
            pos = position;
            isSentiment = false;
            negated = false;
        } else {
            base = tmp[1];
            tag = POS.getPOSTag(tmp[2]);
            pos = position;
            isSentiment = false;
            negated = false;
        }
    }

    public String print() {
        return base + "/" + tag;
    }
}
