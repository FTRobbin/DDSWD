package Linguistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by RobbinNi on 2/6/16.
 */
public class Dictionary {

    public String name;

    private Set<String> list;

    public Dictionary(File dict) throws IOException {
        this.name = dict.getName();
        list = new HashSet<String>();
        BufferedReader bufReader = new BufferedReader(new FileReader(dict));
        String tmp;
        while ((tmp = bufReader.readLine()) != null){
            list.add(tmp.intern());
        }
        bufReader.close();
    }

    public boolean include(Word w) {
        return list.contains(w.base.intern());
    }

    public boolean include(String s) {
        return list.contains(s.intern());
    }

    public int getSize() {
        return list.size();
    }
}
