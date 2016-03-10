package BP;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/22/16.
 */
public class UnionDistribution {

    public ArrayList<RandomVariable> vlist;

    public ArrayList<Double> val;

    private void sizeCheck() {
        int n = vlist.size(), m = val.size();
        if (m != (1 << n)) {
            throw new RuntimeException();
        }
    }

    public UnionDistribution(ArrayList<RandomVariable> vlist, ArrayList<Double> val) {
        this.vlist = vlist;
        this.val = val;
        sizeCheck();
    }

    public UnionDistribution handleNegations() {
        for (int i = 0; i < vlist.size(); ++i) {
            if (vlist.get(i).negated) {
                for (int st = 0; st < val.size(); ++st) {
                    if ((st >> i & 1) == 1) {
                        Double vst = val.get(st);
                        val.set(st, val.get(st ^ (1 << i)));
                        val.set(st ^ (1 << i), vst);
                    }
                }
            }
        }
        return this;
    }


    public UnionDistribution normalize() {
        double sum = 0;
        for (Double d : val) {
            sum += d;
        }
        for (int i = 0; i < val.size(); ++i) {
            val.set(i, val.get(i) / sum);
        }
        return this;
    }

    public UnionDistribution factor(UnionDistribution dist) {
        ArrayList<RandomVariable> nvlist = new ArrayList<RandomVariable>();
        nvlist.addAll(vlist);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (RandomVariable rv : dist.vlist) {
            if (nvlist.contains(rv)) {
                ids.add(nvlist.indexOf(rv));
            } else {
                ids.add(-1);
                nvlist.add(rv);
            }
        }
        ArrayList<Double> nval = new ArrayList<Double>();
        int n = vlist.size(), m = dist.vlist.size();
        for (int i = 0; i < (1 << n); ++i) {
            for (int j = 0; j < (1 << m); ++j) {
                boolean flag = true;
                for (int l = 0; l < m && flag; ++l) {
                    if (ids.get(l) != -1) {
                        if ((i >> (n - 1 - ids.get(l)) & 1) != (j >> (m - 1 - l) & 1)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    nval.add(val.get(i) * dist.val.get(j));
                }
            }
        }
        return (new UnionDistribution(nvlist, nval)).normalize();
    }

    public UnionDistribution marginal(ArrayList<RandomVariable> demargin) {
        ArrayList<RandomVariable> nvlist = new ArrayList<RandomVariable>();
        ArrayList<Boolean> contains = new ArrayList<Boolean>();
        for (RandomVariable rv : vlist) {
            if (demargin.contains(rv)) {
                contains.add(true);
            } else {
                contains.add(false);
                nvlist.add(rv);
            }
        }
        ArrayList<Double> nval = new ArrayList<Double>();
        int nn = nvlist.size();
        for (int i = 0; i < (1 << nn); ++i) {
            nval.add(0.0);
        }
        int n = vlist.size();
        for (int i = 0; i < (1 << n); ++i) {
            int k = 0;
            for (int j = n - 1; j >= 0; --j) {
                if (!contains.get(n - 1 - j)) {
                    k = (k << 1) | (i >> j & 1);
                }
            }
            nval.set(k, nval.get(k) + val.get(i));
        }
        return (new UnionDistribution(nvlist, nval)).normalize();
    }

    public String print() {
        String ret = "";
        for (RandomVariable rv : vlist) {
            ret = ret + rv.name + "|";
        }
        ret = ret + "\n";
        int n = vlist.size();
        for (int i = 0; i < (1 << n); ++i) {
            String line = "";
            for (int j = 0; j < n; ++j) {
                if ((i >> (n - 1 - j) & 1) == 1) {
                    line = line + "1";
                } else {
                    line = line + "0";
                }
            }
            line = line + " : " + val.get(i) + "\n";
            ret = ret + line;
        }
        return ret;
    }
}
