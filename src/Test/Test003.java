package Test;

import BP.Cluster;
import BP.ClusterGraph;

/**
 * Created by RobbinNi on 2/22/16.
 */
public class Test003 {

    public void run(ClusterGraph graph) {
        System.out.println("ClusterGraph Built");
        System.out.println("Nodes : " + graph.nodes.size());
        System.out.println("Edges : " + graph.edges.size());
        for (Cluster c : graph.nodes) {
            System.out.println(c.print());
        }
    }
}
