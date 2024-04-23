package lab5;

import java.util.Set;

public class HasseDiagram {
    private Graph graph;
    private Set<Node> minNodes;

    public HasseDiagram(Graph graph, Set<Node> minNodes) {
        this.graph = graph;
        this.minNodes = minNodes;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Set<Node> getMinNodes() {
        return minNodes;
    }

    public void setMinNodes(Set<Node> minNodes) {
        this.minNodes = minNodes;
    }
}