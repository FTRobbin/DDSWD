package BP;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/22/16.
 */
public class Cluster {

    public UnionDistribution dist;

    public ArrayList<Sepset> in, out;

    public Cluster(UnionDistribution dist) {
        this.dist = dist;
        in = new ArrayList<Sepset>();
        out = new ArrayList<Sepset>();
    }

    public void addEdge(Sepset e) {
        if (e.from == this) {
            out.add(e);
        } else {
            in.add(e);
        }
    }

    public void update() {
        int m = in.size();
        ArrayList<UnionDistribution> suf = new ArrayList<>();
        suf.add(in.get(m - 1).dist);
        for (int i = m - 2; i > 0; --i) {
            suf.add(suf.get(m - i - 2).factor(in.get(i).dist));
        }
        for (int i = 0; i < m; ++i) {
            in.get(i).updateUsed();
        }
        UnionDistribution cur = dist;
        for (int i = 0; i < m; ++i) {
            UnionDistribution nout = i == m - 1 ? cur : cur.factor(suf.get(m - 2 - i));
            out.get(i).updateDist(nout.marginal(out.get(i).demargin));
            cur = cur.factor(in.get(i).dist);
        }
    }

    public UnionDistribution getBelief() {
        UnionDistribution ret = dist;
        for (Sepset e : in) {
            ret = ret.factor(e.dist);
        }
        return ret;
    }

    public String print() {
        String ret = "Cluster " + this.toString() + "\n";
        ret = ret + dist.print();
        ret = ret + "in edges\n";
        for (Sepset s : in) {
            ret = ret + s.print();
        }
        ret = ret + "out edges\n";
        for (Sepset s : out) {
            ret = ret + s.print();
        }
        ret = ret + "===============================\n";
        return ret;
    }
}
