package CSG.Distributions;

import CSG.Distribution;
import Linguistic.Word;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/26/16.
 */
public class Binary implements Distribution {

    public Double similarity;

    public Binary(Double similarity) {
        this.similarity = similarity;
    }

    @Override
    public ArrayList<Double> getDistributionValue(ArrayList<Word> ws) {
        ArrayList<Double> ret = new ArrayList<>();
        ret.add(0.5 * similarity);
        ret.add(0.5 * (1 - similarity));
        ret.add(0.5 * (1 - similarity));
        ret.add(0.5 * similarity);
        return ret;
    }
}
