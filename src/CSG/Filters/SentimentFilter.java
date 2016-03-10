package CSG.Filters;

import CSG.Filter;
import Linguistic.Word;

/**
 * Created by RobbinNi on 2/22/16.
 */
public class SentimentFilter implements Filter {

    @Override
    public boolean isUseful(Word w) {
        return w.isSentiment;
    }
}
