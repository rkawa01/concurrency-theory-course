package lab5;

import java.util.*;

import static lab5.TraceOperations.*;

public class App {

    private static void example1() {
        final Character[] A = {'a', 'b', 'c', 'd'};

        final Set<Tuple> I = new HashSet<>(Arrays.asList(
                new Tuple('a', 'd'),
                new Tuple('d', 'a'),
                new Tuple('b', 'c'),
                new Tuple('c', 'b')
        ));
        final Set<Tuple> D = iToD(I, A);

        System.out.println("D = " + D);

        Character[] a = {'b', 'a', 'a', 'd', 'c', 'b'};
        System.out.println("Postać normalna Foaty: " + foata(a, D));
        System.out.println("Diagram Hassego:");
        HasseDiagram hasseDiagram = hasse(a, D);
        System.out.println(hasseDiagram.getGraph());
        System.out.println("Postać normala Foaty wyznaczona z diagramu: " + foataFromHasseDiagram(hasseDiagram));
        System.out.println("\n");


    }

    private static void example2() {
        final Character[] A = {'a', 'b', 'c', 'd', 'e', 'f'};

        final Set<Tuple> I = new HashSet<>(Arrays.asList(
                new Tuple('a', 'd'),
                new Tuple('d', 'a'),
                new Tuple('b', 'e'),
                new Tuple('e', 'b'),
                new Tuple('c', 'd'),
                new Tuple('d', 'c'),
                new Tuple('c', 'f'),
                new Tuple('f', 'c')
        ));
        final Set<Tuple> D = iToD(I, A);

        System.out.println("D = " + D);

        Character[] a = {'a', 'c', 'd', 'c', 'f', 'b', 'b', 'e'};
        System.out.println("Postać normalna Foaty: " + foata(a, D));
        System.out.println("Diagram Hassego:");
        HasseDiagram hasseDiagram = hasse(a, D);
        System.out.println(hasseDiagram.getGraph());
        System.out.println("Postać normala Foaty wyznaczona z diagramu: " + foataFromHasseDiagram(hasseDiagram));
        System.out.println("\n");
    }

    private static void example3() {
        final Character[] A = {'a', 'b', 'c', 'd', 'e'};

        final Set<Tuple> D = new HashSet<>(Arrays.asList(
                new Tuple('a', 'e'),
                new Tuple('a', 'a'),
                new Tuple('a', 'b'),
                new Tuple('b', 'b'),
                new Tuple('b', 'c'),
                new Tuple('b', 'a'),
                new Tuple('c', 'b'),
                new Tuple('c', 'e'),
                new Tuple('c', 'c'),
                new Tuple('c', 'd'),
                new Tuple('d', 'e'),
                new Tuple('d', 'd'),
                new Tuple('d', 'c'),
                new Tuple('e', 'd'),
                new Tuple('e', 'c'),
                new Tuple('e', 'e'),
                new Tuple('e', 'a')
        ));

        final Set<Tuple> I = iToD(D, A);

        System.out.println("I = " + I);

        Character[] a = {'a', 'c', 'e', 'b', 'd', 'a', 'c'};
        System.out.println("Postać normalna Foaty: " + foata(a, D));
        System.out.println("Diagram Hassego:");
        HasseDiagram hasseDiagram = hasse(a, D);
        System.out.println(hasseDiagram.getGraph());
        System.out.println("Postać normala Foaty wyznaczona z diagramu: " + foataFromHasseDiagram(hasseDiagram));
        System.out.println("\n");
    }



    public static void main(String[] args) {
        example1();
        example2();
        example3();

    }
}