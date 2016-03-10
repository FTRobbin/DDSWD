package Classification;

import BP.Cluster;
import BP.ClusterGraph;
import Linguistic.*;
import CSG.CSG;
import CSG.Rule;
import Linguistic.Dictionary;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by RobbinNi on 3/6/16.
 */
public class Report {

    private ArrayList<Dictionary> dicts;
    private Corpus corpus;
    private ArrayList<CSG> csgs;
    private ClusterGraph graph;
    private ArrayList<Pair<String, Double>> ndist;
    static String cutline = "--------------------\n";

    public Report (ArrayList<Dictionary> dicts, Corpus corpus, ArrayList<CSG> csgs, ClusterGraph graph, ArrayList<Pair<String, Double>> ndist) {
        this.dicts = dicts;
        this.corpus = corpus;
        this.csgs = csgs;
        this.graph = graph;
        this.ndist = ndist;
    }

    private String printDicts() {
        String ret = "" + cutline;
        for (Dictionary dict : dicts) {
            String cur = "";
            cur = cur + "Dictionary : " + dict.name + "  ";
            cur = cur + "Size : " + dict.getSize() + " words\n";
            ret = ret + cur;
        }
        ret = ret + cutline;
        return ret;
    }

    private String printCorpus() {
        String ret = "";
        ret = ret + "Corpus : " + corpus.name + "  ";
        ret = ret + "Size : " + corpus.sentences.size() + " sentences ";

        int wordCnt = 0;
        for (Sentence sen : corpus.sentences) {
            wordCnt += sen.words.size();
        }
        ret = ret + wordCnt + " words\n";

        int posi = 0, nega = 0, neut = 0, negators = 0, negated = 0, nposi = 0, nnega = 0;
        for (Sentence sen : corpus.sentences) {
            negators += sen.negations.size();
            for (Word w : sen.words) {
                if (Utility.isSentiment(w)) {
                    if (w.negated) {
                        ++negated;
                    }
                    if (Utility.getOriginalValue(w.base) > 0.51) {
                        ++posi;
                        if (w.negated) {
                            ++nposi;
                        }
                    } else if (Utility.getOriginalValue(w.base) < 0.49) {
                        ++nega;
                        if (w.negated) {
                            ++nnega;
                        }
                    } else {
                        ++neut;
                    }
                }
            }
        }
        ret = ret + "Sentiments(+/-/o) : " + posi + "/" + nega + "/" + neut + "\n";
        ret = ret + "Negations : " + negators + " negators  " + negated + " negated sentiments : " + nposi + "+/" + nnega + "-\n";
        ret = ret + cutline;
        return ret;
    }

    private String printCSGS() {
        String ret = "";
        ret = ret + "CSGS : " + csgs.size() + " total\n";

        Map<Rule, Integer> count = new HashMap<>();
        for (CSG csg : csgs) {
            count.put(csg.rule, count.getOrDefault(csg.rule, 0) + 1);
        }

        for (Map.Entry<Rule, Integer> entry : count.entrySet()) {
            ret = ret + "Rule \"" + entry.getKey().name() + "\" : " + entry.getValue() + "\n";
        }

        ret = ret + cutline;
        return ret;
    }

    private String printGraph() {
        String ret = "";
        ret = ret + "Nodes : " + graph.nodes.size() + "  Edges : " + graph.edges.size() + "\n";
        int[] cnt = new int[3];
        cnt[0] = cnt[1] = cnt[2] = 0;
        for (Cluster c : graph.nodes) {
            int type = 0;
            if (c.dist.vlist.size() == 1) {
                type = 2;
            } else if (c.dist.vlist.size() == 2 && c.dist.vlist.get(1).type == 1) {
                type = 1;
            } else {
                type = 0;
            }
            cnt[type]++;
        }
        ret = ret + "Node types(0/1/2) : " + cnt[0] + "/" + cnt[1] + "/" + cnt[2] + "\n";
        Map<Integer, Integer> count = new TreeMap<>();
        Map<Integer, ArrayList<String> > words = new TreeMap<>();
        for (Cluster c : graph.nodes) {
            if (c.dist.vlist.size() == 1) {
                count.put(c.out.size(), count.getOrDefault(c.out.size(), 0) + 1);
                if (!words.containsKey(c.out.size())) {
                    words.put(c.out.size(), new ArrayList<>());
                }
                ArrayList<String> tmp = words.get(c.out.size());
                if (tmp.size() < 10) {
                    tmp.add(c.dist.vlist.get(0).name);
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            ret = ret + "#Quotation : " + entry.getKey() + "  Count : " + entry.getValue() + "\n";
            ArrayList<String> tmp = words.get(entry.getKey());
            boolean first = true;
            for (String s : tmp) {
                if (!first) {
                    ret = ret + ",";
                }
                first = false;
                ret = ret + s;
            }
            ret = ret + "\n";
        }
        ret = ret + cutline;
        return ret;
    }

    private String printResultList(ArrayList<Pair<String, Double>> result) {
        String ret = "";
        int cnt = 0;
        for (Pair<String, Double> pair : result) {
            ++cnt;
            if (cnt > 20) {
                break;
            }
            ret = ret + pair.getKey() + " : " + pair.getValue() + "\n";
        }
        ret = ret + "\n";
        return ret;
    }

    private String printResult() {
        String ret = "";
        ArrayList<Pair<String, Double>> posiP = new ArrayList<>(),
                                        posiN = new ArrayList<>(),
                                        neutP = new ArrayList<>(),
                                        neutN = new ArrayList<>(),
                                        negaP = new ArrayList<>(),
                                        negaN = new ArrayList<>();
        for (Pair<String, Double> pair : ndist) {
            Double ori = Utility.getOriginalValue(pair.getKey());
            if (ori > 0.51) {
                if (pair.getValue() > ori) {
                    posiP.add(pair);
                } else {
                    posiN.add(pair);
                }
            } else if (ori < 0.49) {
                if (pair.getValue() > ori) {
                    negaP.add(pair);
                } else {
                    negaN.add(pair);
                }
            } else {
                if (pair.getValue() > ori) {
                    neutP.add(pair);
                } else {
                    neutN.add(pair);
                }
            }
        }
        Comparator<Pair<String, Double>> greater = new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
                if (o1.getValue() > o2.getValue()) {
                    return -1;
                } else if (o1.getValue() < o2.getValue()) {
                    return 1;
                }
                return 0;
            }
        };
        Comparator<Pair<String, Double>> lesser = new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
                if (o1.getValue() > o2.getValue()) {
                    return 1;
                } else if (o1.getValue() < o2.getValue()) {
                    return -1;
                }
                return 0;
            }
        };
        posiP.sort(greater);
        posiN.sort(lesser);
        neutP.sort(greater);
        neutN.sort(lesser);
        negaP.sort(greater);
        negaN.sort(lesser);
        ret = ret + "Positive + : \n" + printResultList(posiP);
        ret = ret + "Positive - : \n" + printResultList(posiN);
        ret = ret + "Negative + : \n" + printResultList(negaP);
        ret = ret + "Negative - : \n" + printResultList(negaN);
        ret = ret + "Neutral + : \n" + printResultList(neutP);
        ret = ret + "Neutral - : \n" + printResultList(neutN);
        ret = ret + cutline;
        return ret;
    }

    public String print() {
        return "Report : \n" + printDicts() + printCorpus() + printCSGS() + printGraph() + printResult();
    }
}
