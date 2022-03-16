package leetcode;

import org.junit.Test;

public class LC363 {

    @Test
    public void test() {
        int[][] matrix = new int[][]{{2, 2, -1}};
        int k = 3;
        int res = maxSumSubmatrix(matrix, k);
        System.out.println(res);
    }

    public int maxSumSubmatrix(int[][] matrix, int k) {
        int m = matrix.length, n = matrix[0].length;
        int[][] a = new int[m][n];
        a[0][0] = matrix[0][0];
        int max = a[0][0];
        if (a[0][0] == k) return k;
        for (int i = 1; i < m; i++) {
            a[i][0] = a[i - 1][0] + matrix[i][0];
            if (matrix[i][0] == k || a[i][0] == k) return k;
            max = Math.max(max, a[i][0]);
            max = Math.max(max, matrix[i][0]);
        }

        for (int j = 1; j < n; j++) {
            a[0][j] = a[0][j - 1] + matrix[0][j];
            if (matrix[0][j] == k || a[0][j] == k) return k;
            max = Math.max(max, a[0][j]);
            max = Math.max(max, matrix[0][j]);
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                a[i][j] = a[i - 1][j] + a[i][j - 1] + matrix[i][j] - a[i - 1][j - 1];
                if (matrix[i][j] == k || a[i][j] == k) return k;
                max = Math.max(max, a[i][j]);
                max = Math.max(max, matrix[i][j]);
            }
        }

        for (int i1 = 0; i1 < m - 1; i1++) {
            for (int j1 = 0; j1 < n - 1; j1++) {
                int u = i1 == 0 ? 0 : a[i1 - 1][j1];
                int l = j1 == 0 ? 0 : a[i1][j1 - 1];
                int ul = (i1 == 0 || j1 == 0) ? 0 : a[i1 - 1][j1 - 1];
                for (int i2 = i1; i2 < m; i2++) {
                    for (int j2 = j1; j2 < n; j2++) {
                        max = Math.max(max, a[i2][j2] - u - l + ul);
                        if (max == k) return k;
                    }
                }
            }
        }
        return max;
    }
}
