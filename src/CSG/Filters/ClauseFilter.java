package CSG.Filters;

import CSG.Filter;
import Linguistic.POS;
import Linguistic.Utility;
import Linguistic.Word;

/**
 * Created by RobbinNi on 2/21/16.
 */
public class ClauseFilter implements Filter {

    @Override
    public boolean isUseful(Word w) {
        return Utility.isSentiment(w)
                || w.tag.equals(POS.SYMFS)
                || w.tag.equals(POS.SYMPE)
                || w.tag.equals(POS.SYMQM);
    }
}
