package Classification;

import BP.Cluster;
import BP.ClusterGraph;
import BP.RandomVariable;
import BP.UnionDistribution;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 3/6/16.
 */
public class Classifier {

    public static ArrayList<Pair<String, Double>> calcDistribution(ClusterGraph graph) {
        ArrayList<Pair<String, Double>> ret = new ArrayList<>();
        for (Cluster c : graph.nodes) {
            if (c.dist.vlist.size() == 1 && c.dist.vlist.get(0).type == 1) {
                UnionDistribution dist = c.getBelief();
                RandomVariable rv = dist.vlist.get(0);
                ret.add(new Pair<>(rv.name, dist.val.get(0)));
            }
        }
        return ret;
    }
}
