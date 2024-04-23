from fnf import analyze_problem

filenames = ["example1.txt", "example2.txt"]
draw_graphs = True


def main():
    for file in filenames:
        print("Analyzing problem defined in file: ", file)
        analyze_problem(file, draw_graphs)
        print("==========================================")


main()
