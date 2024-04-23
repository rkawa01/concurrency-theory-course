import threading

class GaussMatrix:
    def __init__(self):
        self.N = 0
        self.values = []
        self.m = []
        self.n = []
        self.eps = 1e-10

    def numbers_from_line(self, line, N):
        result = []
        start = 0
        end = line.find(' ')
        for _ in range(N):
            token = line[start:end]
            result.append(float(token))
            start = end + 1
            end = line.find(' ', start)
        return result

    def actionA(self, i, k):
        self.m[k] = self.values[k][i] / self.values[i][i]

    def actionB(self, i, j, k):
        self.n[k][j] = self.values[i][j] * self.m[k]

    def actionC(self, j, k):
        self.values[k][j] -= self.n[k][j]

    def pivoting(self, i):
        if abs(self.values[i][i]) < self.eps:
            j = i + 1
            while j < self.N and abs(self.values[j][i]) < self.eps:
                j += 1
            if j == self.N:
                raise ValueError("Could not perform pivoting.")
            for k in range(i, self.N + 1):
                self.values[i][k], self.values[j][k] = self.values[j][k], self.values[i][k]

    def read_from_file(self, filename):
        with open(filename, 'r') as file:
            line = file.readline().strip()
            self.N = int(line)

            A = []
            for _ in range(self.N):
                line = file.readline().strip()
                row = self.numbers_from_line(line, self.N)
                A.append(row)

            line = file.readline().strip()
            Y = self.numbers_from_line(line, self.N)

            self.init(self.N, A, Y)

    def write_to_file(self, filename):
        with open(filename, 'w') as file:
            file.write(str(self))

    def init(self, N, A, Y):
        self.N = N
        self.values = [[0] * (N + 1) for _ in range(N)]
        self.m = [0] * N
        self.n = [[0] * (N + 1) for _ in range(N)]
        for i in range(N):
            for j in range(N):
                self.values[i][j] = A[i][j]
            self.values[i][N] = Y[i]

    def get_N(self):
        return self.N

    def concurrent_gauss(self):
        threads = []
        for i in range(self.N - 1):
            self.pivoting(i)

            # F_A,i
            threads = []
            for j in range(i + 1, self.N):
                threadA = threading.Thread(target=self.actionA, args=(i, j))
                threads.append(threadA)
            for t in threads:
                t.start()
            for t in threads:
                t.join()

            # F_B,i
            threads = []
            for k in range(i + 1, self.N):
                for j in range(i, self.N + 1):
                    threadB = threading.Thread(target=self.actionB, args=(i, j, k))
                    threads.append(threadB)
            for t in threads:
                t.start()
            for t in threads:
                t.join()

            # F_C,i
            threads = []
            for k in range(i + 1, self.N):
                for j in range(i, self.N + 1):
                    threadC = threading.Thread(target=self.actionC, args=(j, k))
                    threads.append(threadC)
            for t in threads:
                t.start()
            for t in threads:
                t.join()

    def back_substitution(self):
        for i in range(self.N - 1, -1, -1):
            for j in range(self.N - 1, i, -1):
                self.values[i][self.N] -= self.values[i][j] * self.values[j][self.N]
                self.values[i][j] = 0
            self.values[i][self.N] /= self.values[i][i]
            self.values[i][i] = 1

    def __getitem__(self, key):
        return self.values[key[0]][key[1]]

    def __str__(self):
        result = f"{self.N}\n"
        for i in range(self.N):
            for j in range(self.N):
                result += f"{self.values[i][j]:.1f} "
            result += "\n"
        for i in range(self.N):
            result += f"{self.values[i][self.N]:.1f} "
        return result
    
if __name__ == "__main__":
    filename = "matrix_input.txt"
    output_filename = "matrix_output.txt"
    matrix = GaussMatrix()
    matrix.read_from_file(filename)
    matrix.concurrent_gauss()
    matrix.back_substitution()
    matrix.write_to_file(output_filename)
