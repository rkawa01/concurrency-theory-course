import gauss

def main():
    filename = "matrix_input.txt"
    output_filename = "matrix_output.txt"
    matrix = gauss.GaussMatrix()
    matrix.read_from_file(filename)
    matrix.concurrent_gauss()
    matrix.back_substitution()
    matrix.write_to_file(output_filename)

if __name__ == "__main__":
    main()