package lab5;

import java.io.Serializable;

public class Edge implements Serializable{
    private Node fromNode;
    private Node toNode;

    public Edge(Node fromNode, Node toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public void setFromNode(Node fromNode) {
        this.fromNode = fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public void setToNode(Node toNode) {
        this.toNode = toNode;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Edge edge = (Edge) o;

        if (!fromNode.equals(edge.fromNode))
            return false;
        return toNode.equals(edge.toNode);
    }

    @Override public int hashCode() {
        int result = fromNode.hashCode();
        result = 31 * result + toNode.hashCode();
        return result;
    }

    @Override public String toString() {
        return fromNode + " -> " + toNode;
    }
}
