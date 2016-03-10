package CSG.Filters;

import Linguistic.POS;
import Linguistic.Word;
import Linguistic.Utility;
import CSG.Filter;

/**
 * Created by RobbinNi on 2/21/16.
 */
public class ConjunctionFilter implements Filter {

    @Override
    public boolean isUseful(Word w) {
        return Utility.isSentiment(w)
                || w.tag.equals(POS.CC);
    }
}
