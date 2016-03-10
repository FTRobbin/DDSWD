package Test;

import Linguistic.Corpus;
import Linguistic.Sentence;

import java.io.File;
import java.io.IOException;

import static java.lang.System.exit;

/**
 * Created by RobbinNi on 2/6/16.
 */
public class Test001 {

    public void run() {
        System.out.println("Test001 : Hello World!");
        String washerPath = "res/corpus/washer.txt";
        Corpus washer = null;
        try {
            washer = new Corpus(new File(washerPath));
        } catch (IOException ie) {
            ie.printStackTrace();
            exit(1);
        }
        Sentence sen = washer.sentences.get(0);
        System.out.println(sen.print());
    }
}
