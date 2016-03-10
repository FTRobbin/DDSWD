package BP;

import java.util.ArrayList;

/**
 * Created by RobbinNi on 2/22/16.
 */
public class ClusterGraph {

    public ArrayList<Cluster> nodes;

    public ArrayList<Sepset> edges;

    public ClusterGraph() {
        nodes = new ArrayList<Cluster>();
        edges = new ArrayList<Sepset>();
    }

    public void addNode(Cluster c) {
        nodes.add(c);
    }

    public void addEdge(Sepset e) {
        edges.add(e);
        e.from.addEdge(e);
        e.to.addEdge(e);
    }
}
