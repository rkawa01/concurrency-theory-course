package lab5;

import java.io.Serializable;

public class Node implements Serializable {
    private int id;
    private char label;

    public Node(int id, char label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getLabel() {
        return label;
    }

    public void setLabel(char label) {
        this.label = label;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Node node = (Node) o;

        return id == node.id;
    }

    @Override public int hashCode() {
        return id;
    }

    @Override public String toString() {
        return id + "[label=" + label + "]";
    }
}
