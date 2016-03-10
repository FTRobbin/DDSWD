package BP;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/22/16.
 */
public class Sepset {

    public Cluster from, to;

    public ArrayList<RandomVariable> demargin;

    public UnionDistribution dist;

    public double lastUsed;

    public Sepset(Cluster from, Cluster to) {
        this.from = from;
        this.to = to;
        demargin = new ArrayList<RandomVariable>();
        ArrayList<RandomVariable> sepset = new ArrayList<RandomVariable>();
        for (RandomVariable rv : from.dist.vlist) {
            if (to.dist.vlist.contains(rv)) {
                sepset.add(rv);
            } else {
                demargin.add(rv);
            }
        }
        ArrayList<Double> init = new ArrayList<Double>();
        int n = (1 << (from.dist.vlist.size() - demargin.size()));
        for (int i = 0; i < n; ++i) {
            init.add(1.0 / n);
        }
        dist = new UnionDistribution(sepset, init);
        lastUsed = 0;
    }

    public double getDelta() {
        return Math.abs(dist.val.get(0) - lastUsed);
    }

    public void updateUsed() {
        lastUsed = dist.val.get(0);
    }

    public void updateDist(UnionDistribution dist) {
        this.dist = dist;
    }

    public String print() {
        String ret = "";
        ret = "From : " + from.toString() + " To : " + to.toString() + "\n";
        ret = ret + dist.print();
        return ret;
    }
}
