package dev.core;

import org.junit.Test;

public class MatrixMult {

    @Test
    public void testMatrixMult() {
        for (int n=10; n<2600; n=n*2) {
            multMatrix(n, n%13);
        }
    }

    public void multMatrix(int N, int shift) {
        long[][] matrix1 = createMatrix(N, shift);
        long[][] matrix2 = createMatrix(N, shift+1);

        long[][] res = new long[N][N];
        long start = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    res[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        long time1= System.currentTimeMillis() - start;

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                res[i][j] = 0;

        start = System.currentTimeMillis();
        long[][] tmp = new long[N][N];
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                tmp[i][j] = matrix2[j][i];

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                for (int k = 0; k < N; ++k)
                    res[i][j] += matrix1[i][k] * tmp[j][k];//todo alyuand j k

        long time2= System.currentTimeMillis() - start;
        System.out.println("N: " + N + ", time1: " + time1 + ", time2: " + time2);
    }

    private long[][] createMatrix(int n, int shift) {
        long[][] res = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = (i + 1 + j + 1) % 199 + shift;
            }
        }
        return res;
    }
}
