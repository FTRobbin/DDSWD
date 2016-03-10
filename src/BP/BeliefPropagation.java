package BP;

import Linguistic.Dictionary;
import CSG.CSG;
import Linguistic.Utility;
import Linguistic.Word;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by RobbinNi on 2/22/16.
 */
public final class BeliefPropagation {

    private static ClusterGraph ret;

    private static Map<Word, RandomVariable> rvMap;

    private static Map<Word, Cluster> clMap;

    private static Map<String, Cluster> clMap2;

    private static RandomVariable getRandomVariable(Word w) {
        if (!rvMap.containsKey(w)) {
            rvMap.put(w, new RandomVariable(w.base + w.toString(), 0, w.negated));
        }
        return rvMap.get(w);
    }

    private static ArrayList<Double> getSingleDistribution(String s) {
        ArrayList<Double> ret = new ArrayList<>();
        ret.add(Utility.getOriginalValue(s));
        ret.add(1.0 - ret.get(0));
        return ret;
    }

    private static ArrayList<Double> getBoundedDistribution() {
        ArrayList<Double> ret = new ArrayList<>();
        ret.add(0.35);
        ret.add(0.15);
        ret.add(0.15);
        ret.add(0.35);
        return ret;
    }

    private static Cluster getCluster(Word w) {
        if (!clMap.containsKey(w)) {
            if (!clMap2.containsKey(w.base.intern())) {
                ArrayList<RandomVariable> rvs = new ArrayList<>();
                rvs.add(new RandomVariable(w.base, 1, false));
                ArrayList<Double> vals = getSingleDistribution(w.base);
                Cluster nclu = new Cluster(new UnionDistribution(rvs, vals));
                ret.addNode(nclu);
                clMap2.put(w.base.intern(), nclu);
            }
            Cluster top = clMap2.get(w.base.intern());
            ArrayList<RandomVariable> rvs = new ArrayList<>();
            rvs.add(getRandomVariable(w));
            rvs.add(top.dist.vlist.get(0));
            ArrayList<Double> vals = getBoundedDistribution();
            Cluster nclu = new Cluster((new UnionDistribution(rvs, vals)).handleNegations());
            ret.addNode(nclu);
            ret.addEdge(new Sepset(nclu, top));
            ret.addEdge(new Sepset(top, nclu));
            clMap.put(w, nclu);
        }
        return clMap.get(w);
    }

    public static ClusterGraph buildGraph(ArrayList<CSG> csgs) {
        ret = new ClusterGraph();
        rvMap = new HashMap<>();
        clMap = new HashMap<>();
        clMap2 = new HashMap<>();
        for (CSG csg : csgs) {
            ArrayList<RandomVariable> rvs = new ArrayList<>();
            for (Word w : csg.sw) {
                rvs.add(getRandomVariable(w));
            }
            ArrayList<Double> vals = csg.getDistributionValue();
            Cluster cur = new Cluster(new UnionDistribution(rvs, vals));
            ret.addNode(cur);
            for (Word w : csg.sw) {
                Cluster nxt = getCluster(w);
                ret.addEdge(new Sepset(cur, nxt));
                ret.addEdge(new Sepset(nxt, cur));
            }
        }
        return ret;
    }

    public static void beliefPropagation(ClusterGraph g) {
        PriorityQueue<Pair<Double, Sepset>> heap = new PriorityQueue<>(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Double key1 = ((Pair<Double, Sepset>)o1).getKey(),
                        key2 = ((Pair<Double, Sepset>)o2).getKey();
                if (key1 > key2) {
                    return -1;
                } else if (key1 < key2) {
                    return 1;
                }
                return 0;
            }
        });
        for (Sepset e : g.edges) {
            heap.add(new Pair<Double, Sepset>(e.getDelta(), e));
        }
        int BAR = 5000000;
        int updateCnt = 0;
        double lastDelta = 0;
        while (BAR > 0 && heap.size() > 0) {
            --BAR;
            Pair<Double, Sepset> head = heap.poll();
            Double d = head.getKey();
            Sepset e = head.getValue();
            if (e.getDelta() != d) {
                continue;
            } else {
                ++updateCnt;
                lastDelta = e.getDelta();
                e.to.update();
                for (Sepset ne : e.to.out) {
                    heap.add(new Pair<Double, Sepset>(ne.getDelta(), ne));
                }
            }
        }
        System.err.println("Belief Propagation Completed. " + updateCnt + " updates, lastdelta = " + lastDelta + ".");
    }
}
