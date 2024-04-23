package lab5;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Graph implements Serializable {
    private Set<Node> nodes;
    private Set<Edge> edges;

    public Graph() {
        this.nodes = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    @Override public String toString() {
        StringBuilder str = new StringBuilder("digraph g{\n");
        for(Edge edge : edges) {
            str.append(edge.getFromNode().getId())
                    .append(" -> ")
                    .append(edge.getToNode().getId())
                    .append("\n");
        }

        for(Node node : nodes) {
            str.append(node.toString())
                    .append("\n");
        }

        str.append("}");

        return str.toString();
    }
}