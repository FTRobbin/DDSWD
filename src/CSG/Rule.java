package CSG;

import Linguistic.Sentence;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/21/16.
 */

public abstract class Rule {

    public Filter f;

    public Distribution d;

    public Rule(Filter filter, Distribution dist) {
        f = filter;
        d = dist;
    }

    public abstract ArrayList<CSG> apply(Sentence se);

    public abstract String name();
}
