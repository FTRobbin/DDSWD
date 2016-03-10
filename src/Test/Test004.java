package Test;

import BP.Cluster;
import BP.ClusterGraph;
import BP.RandomVariable;
import BP.UnionDistribution;
import Linguistic.Dictionary;

/**
 * Created by RobbinNi on 2/22/16.
 */
public class Test004 {

    public void run(ClusterGraph graph, Dictionary positive, Dictionary negative) {
        for (Cluster c : graph.nodes) {
            if (c.dist.vlist.size() == 1 && c.dist.vlist.get(0).type == 1) {
                UnionDistribution dist = c.getBelief();
                RandomVariable rv = dist.vlist.get(0);
                String symbol = positive.include(rv.name) ? "+" : (negative.include(rv.name) ? "-" : "o");
                System.out.println(rv.name + " " + symbol + " " + dist.val.get(0) + " " + dist.val.get(1));
            }
        }
    }
}
