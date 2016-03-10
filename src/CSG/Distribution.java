package CSG;

import Linguistic.Word;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/26/16.
 */
public interface Distribution {

    public ArrayList<Double> getDistributionValue(ArrayList<Word> ws);
}
