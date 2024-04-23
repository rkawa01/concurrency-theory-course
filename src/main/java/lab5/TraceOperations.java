package lab5;

import org.apache.commons.lang.SerializationUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TraceOperations {

    /**
     * Get Hasse diagram for the given trace
     * @param a given trace
     * @param d set of dependencies (D)
     * @return HasseDiagram object containing Hasse diagram and minimal nodes set
     */
    public static HasseDiagram hasse(Character[] a, Set<Tuple> d) {
        Graph h = new Graph();
        Set<Node> min = new HashSet<>();

        for(int i = a.length - 1; i >= 0; i--) {
            Node v = new Node(i, a[i]);
            h.addNode(v);
            min.add(v);

            Iterator<Node> iter = min.iterator();
            while(iter.hasNext()) {
                Node x = iter.next();
                if (v != x) {
                    if (d.contains(new Tuple(v.getLabel(), x.getLabel()))) {
                        Edge e = new Edge(v, x);
                        h.addEdge(e);
                    }
                }
            }
        }

        h.getNodes().forEach(x ->
                h.getNodes().forEach(y ->
                        h.getNodes().forEach(z -> {
                            Edge xz = new Edge(x, z);
                            if(h.getEdges().contains(xz)) {
                                if(h.getEdges().contains(new Edge(x, y)) && h.getEdges().contains(new Edge(y, z))) {
                                    h.getEdges().remove(xz);
                                }
                            }
                        })
                ));

        return new HasseDiagram(h, min);
    }

    /**
     * Get FNF from Hasse diagram
     * @param diagram Hasse diagram object
     * @return FNF string, eg. (a)(bc)(e)(d)
     */
    public static String foataFromHasseDiagram(HasseDiagram diagram) {
        Graph copiedGraph = (Graph) SerializationUtils.clone(diagram.getGraph());

        List<Node> currentNodes = new LinkedList<>();
        StringBuilder stringBuilder = new StringBuilder();
        do {
            currentNodes.clear();

            copiedGraph.getNodes()
                    .forEach(node -> {
                        if(copiedGraph.getEdges().stream().noneMatch(edge -> edge.getToNode() == node)) {
                            currentNodes.add(node);

                        }
                    });
            if(!currentNodes.isEmpty()) {
                stringBuilder.append("(");
                currentNodes.sort(Comparator.comparingInt(Node::getLabel));
                currentNodes.forEach(node -> stringBuilder.append(node.getLabel()));
                stringBuilder.append(")");
            }


            Set<Node> toDelete = new HashSet<>();
            currentNodes.forEach(node -> {
                copiedGraph.getEdges().removeIf(edge -> edge.getFromNode() == node);
                toDelete.add(node);
            });

            toDelete.forEach(node -> {
                copiedGraph.getNodes().remove(node);
            });

        } while (!currentNodes.isEmpty());

        return stringBuilder.toString();
    }


    /**
     * Get D set from I set (or reverse)
     * @param I I set (or D if reverse)
     * @param A alphabet we are working on
     * @return D set (or I if reverse)
     */
    public static Set<Tuple> iToD(Set<Tuple> I, Character[] A) {
        Set<Tuple> D = new HashSet<>();
        Arrays.stream(A).forEach(c -> {
            Arrays.stream(A).forEach(d -> {
                Tuple tup = new Tuple(c, d);
                if(!I.contains(tup)) {
                    D.add(tup);
                }
            });
        });

        return D;
    }

    /**
     * Get FNF for the given trace
     * @param a given trace
     * @param deps set of dependencies (D)
     * @return FNF string, eg. (a)(bc)(e)(d)
     */
    public static String foata(Character[] a, Set<Tuple> deps) {
        Character[] unique = Arrays.stream(a).distinct().toArray(Character[]::new);
        Map<Character, Stack<Character>> stacks = new HashMap<>();
        Arrays.stream(unique).forEach(c -> stacks.put(c, new Stack<>()));

        for (int i = a.length - 1; i >= 0; i--) {
            char o = a[i];
            Stack<Character> st = stacks.get(o);
            st.push(o);
            deps.forEach(dep -> {
                if (dep.getDepFrom() == o && dep.getDepFrom() != dep.getDepTo()) {
                    Stack<Character> s = stacks.get(dep.getDepTo());
                    s.push('*');
                }
            });
        }

        StringBuilder strBuilder = new StringBuilder("");
        while(true) {
            if(Arrays.stream(unique).allMatch(u -> stacks.get(u).empty())) {
                break;
            }
            strBuilder.append("(");
            Set<Character> withLetterOnTop = Arrays.stream(unique)
                    .filter(u -> !stacks.get(u).empty() && stacks.get(u).peek() != '*')
                    .collect(Collectors.toSet());

            withLetterOnTop.forEach(u -> {
                Stack<Character> stack = stacks.get(u);
                if(stack.peek() != '*') {
                    char o = stack.pop();
                    strBuilder.append(o);
                    deps.stream()
                            .filter(dep -> dep.getDepFrom() == o && dep.getDepTo() != o)
                            .forEach(dep -> {
                                Stack<Character> g = stacks.get(dep.getDepTo());
                                if(g.peek() == '*') {
                                    g.pop();
                                }
                            });

                }
            });
            strBuilder.append(")");
        }

        return strBuilder.toString();
    }
}
