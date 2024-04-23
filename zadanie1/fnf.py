import re


def parse_input(filename):
    """
    Parses the input file and returns the alphabet, word and relations from the file.
    File should be structured as follows:
    A = {a, b, c, d}
    w = baadcb
    (a) x := x + y
    (b) y := y + 2z
    (c) x := 3x + z
    (d) z := y - z

    :param filename: name of the file to be parsed
    :return: alphabet, word, relations
    """
    A = []
    w = ""
    relations = {}
    with open(filename) as f:
        # First line defines the alphabet
        line = f.readline()
        A = set(map(str.strip,
                    line[line.find("{")+1:line.find("}")].split(",")))

        # Second line defines the word
        w = f.readline().strip().split("= ")[1]

        # Next lines define the relations
        for line in f:
            relation = line.strip().split(":=")
            relation_unparsed_id, relation_value = relation[0], relation[1]

            # Parse index
            closing_bracket_index = relation_unparsed_id.find(")")
            relation_id = relation_unparsed_id[relation_unparsed_id.find(
                "(")+1:closing_bracket_index]
            relation_variable = relation_unparsed_id[closing_bracket_index +
                                                     1:].strip()

            # Parse value - obtain all variables from the text
            matches = set(re.findall("[a-z]+", relation_value))

            relations[relation_id] = [relation_variable, matches]

    return A, w, relations


def dependency_graph(relations):
    """
    Creates a dependency graph from the relations parsed in the input file.

    :param relations: relations parsed from the input file
    :return: dependency graph
    """
    variable_positions_left_on_assign = {}
    for relation_id, relation in relations.items():
        if relation[0] not in variable_positions_left_on_assign:
            variable_positions_left_on_assign[relation[0]] = set()
        variable_positions_left_on_assign[relation[0]].add(relation_id)

    variable_positions_right_on_assign = {}
    for relation_id, relation in relations.items():
        for variable in relation[1]:
            if variable not in variable_positions_right_on_assign:
                variable_positions_right_on_assign[variable] = set()
            variable_positions_right_on_assign[variable].add(relation_id)

    D = {}
    for variable, relation in relations.items():
        D[variable] = set()
        for relation_variable in relation[1]:
            D[variable].update(
                variable_positions_left_on_assign[relation_variable])
        D[variable].update(variable_positions_right_on_assign[relation[0]])

    return D


def independency_graph(A, D):
    """
    Creates an independency graph from the dependency graph.

    :param A: alphabet
    :param D: dependency graph
    :return: independency graph
    """
    I = {}

    for relation_id in D:
        I[relation_id] = A - D[relation_id]

    return I


def convert_to_edge_list(G):
    """
    Converts a graph to an edge list.

    :param G: graph
    :return: edge list
    """
    g_pairs = []
    for relation_id in G:
        for variable in G[relation_id]:
            g_pairs.append((relation_id, variable))

    return sorted(g_pairs)


def get_words_dependency_graph(D, w):
    """
    Creates a dependency graph for a word w.

    :param D: dependency graph
    :param w: word

    :return: dependency graph for word w
    """
    w_dependence_graph = {}

    for i in range(len(w)):
        # Look from the right of letter at index i and create edges
        x = w[i]
        if x not in w_dependence_graph:
            w_dependence_graph[i] = set()

        for j in range(i+1, len(w)):
            y = w[j]
            if y in D[x]:
                w_dependence_graph[i].add(j)

    return w_dependence_graph


def foata_normal_form(A, D, w):
    """
    Creates a Foata normal form for a word w.

    :param A: alphabet
    :param D: dependency graph
    :param w: word
    :return: Foata normal form for word w
    """
    
    stacks = {k: [] for k in A}

    # Fill stacks
    for i in range(len(w)-1, -1, -1):
        x = w[i]
        stacks[x].append(x)
        for y in D[x]:
            if y != x:
                stacks[y].append('*')

    fnf = []
    while True:
        if all(len(stack) == 0 for stack in stacks.values()):
            return "".join(fnf)

        fnf.append('(')
        letters_on_top = set(
            c for c in A if stacks[c] and stacks[c][-1] != '*')

        for u in letters_on_top:
            if stacks[u][-1] != '*':
                v = stacks[u].pop()
                fnf.append(v)
                for y in D[v]:
                    if stacks[y] and stacks[y][-1] == '*' and y != v:
                        stacks[y].pop()

        fnf.append(')')


def create_hasse_diagram(w, w_dependence_graph):
    """
    Creates a Hasse diagram for a word w.

    :param w: word
    :param w_dependence_graph: dependency graph for word w
    :return: Hasse diagram for word w
    """
    # Create Hasse diagram
    hasse_diagram = {}
    min_subset = set()

    for i in range(len(w) - 1, -1, -1):
        # Create a new node i with label w[i]
        hasse_diagram[i] = set()
        min_subset.add(i)

        for x in min_subset:
            if x == i:
                continue

            if x in w_dependence_graph[i]:
                hasse_diagram[i].add(x)

    # Do the transitive reduction
    for i in range(len(w)):
        for j in range(len(w)):
            for k in range(len(w)):
                if j in hasse_diagram[i] and k in hasse_diagram[j] and k in hasse_diagram[i]:
                    hasse_diagram[i].remove(k)

    return hasse_diagram


def generate_dot_file_for_graph(w, G):
    """
    Generates a dot file for a graph.

    :param G: graph
    :return: dot file
    """
    dot_file = "digraph G {\n"
    for node in G:
        for neighbour in G[node]:
            dot_file += "\t" + str(node) + " -> " + str(neighbour) + "\n"

    dot_file += "\n"
    for i in range(len(w)):
        dot_file += "\t" + str(i) + " [label=\"" + w[i] + "\"]\n"
    dot_file += "}"
    return dot_file


def draw_graph_using_nx(w, G):
    """
    Draws a graph.

    :param G: graph
    :return: dot file
    """
    import networkx as nx
    import matplotlib.pyplot as plt

    G = nx.MultiDiGraph(G)
    layout = nx.spring_layout(G)
    nx.draw(G, pos=layout, with_labels=False)
    nx.draw_networkx_labels(G, pos=layout, labels={
                            i: w[i] for i in range(len(w))})
    plt.show()


def analyze_problem(filename, draw_graph=False):
    """
    Analyzes the problem defined in the input file.

    :param filename: name of the file to be parsed
    :return: alphabet, word, relations, dependency graph, independency graph
    """
    A, w, relations = parse_input(filename)
    D = dependency_graph(relations)
    I = independency_graph(A, D)
    w_dependence_graph = get_words_dependency_graph(D, w)
    fnf = foata_normal_form(A, D, w)
    hasse_diagram = create_hasse_diagram(w, w_dependence_graph)
    dot_graph = generate_dot_file_for_graph(w, hasse_diagram)

    print("A =", A)
    print("w =", w)
    print("D =", convert_to_edge_list(D))
    print("I =", convert_to_edge_list(I))
    print("Foata normal form: ", fnf)
    print("Hasse diagram: ", convert_to_edge_list(hasse_diagram))
    print("Hasse diagram dot file: \n", dot_graph)
    if draw_graph:
        draw_graph_using_nx(w, hasse_diagram)

    # Write output to the file
    no_ext_filename = "".join(filename.split(".")[:-1])
    with open(f"{no_ext_filename}_output.txt", "w") as f:
        f.write("Alphabet: " + str(A) + "\n")
        f.write("Word: " + str(w) + "\n")
        f.write("Dependency graph: " + str(convert_to_edge_list(D)) + "\n")
        f.write("Independency graph: " + str(convert_to_edge_list(I)) + "\n")
        f.write("Word's dependence graph: " + str(w_dependence_graph) + "\n")
        f.write("Foata normal form: " + str(fnf) + "\n")
        f.write("Hasse diagram: " +
                str(convert_to_edge_list(hasse_diagram)) + "\n")
        f.write("Hasse diagram dot file: \n" +
                str(dot_graph) + "\n")

    with open(f"{no_ext_filename}.dot", "w") as f:
        f.write(dot_graph)
