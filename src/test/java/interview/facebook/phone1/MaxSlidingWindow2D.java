package interview.facebook.phone1;

import org.junit.Test;

import java.util.TreeMap;

public class MaxSlidingWindow2D {

    @Test
    public void test() {
        int[][] mat = new int[][]{{1, 3, 2}, {2, 1, 3}, {0, 4 ,0}};
        int k = 2;
        int[][] solve = solve(mat, k);
    }
    /**
     * Given a int m * m matrix, and an int k,
     * there is a 2D window(k * k) that moves from top-left to bottom right
     * return a 2d int array that represents the max value in a window when the window moves.
     *
     * e.g. m = 3, k = 2, matrix:
     * [1, 3, 2]
     * [2, 1, 3]
     * [0, 4, 0]
     * res is:
     * [3, 3]
     * [4, 4]
     *
     * assume the given params are legal
     */

    public int[][] solve(int[][] mat, int k) {
        int m = mat.length, n = m - k + 1;
        // key is an element in mat, value is freq of the element
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                map.put(mat[i][j], map.getOrDefault(mat[i][j], 0) + 1);
            }
        }

        int[][] res = new int[n][n];
        TreeMap<Integer, Integer> up = map;
        for (int i = 0; i < n; i++) {
            TreeMap<Integer, Integer> cur = new TreeMap<>(up);
            // move window 1 row down
            for (int p = 0; p < k; p++) {
                int v = mat[i + k - 1][p];
                cur.put(v, cur.getOrDefault(v ,0) + 1);
            }
            if (i > 0) {
                for (int p = 0; p < k; p++) {
                    int v = mat[i - 1][p];
                    if (cur.get(v) == 1) cur.remove(v);
                    else cur.put(v, cur.get(v) - 1);
                }
            }
            res[i][0] = cur.lastKey();
            up = new TreeMap<>(cur);

            for (int j = 1; j < n; j++) {
                for (int p = i; p < i + k; p++) {
                    int v = mat[p][j + k - 1];
                    cur.put(v, cur.getOrDefault(v ,0) + 1);
                }
                for (int p = i; p < i + k; p++) {
                    int v = mat[p][j - 1];
                    if (cur.get(v) == 1) cur.remove(v);
                    else cur.put(v, cur.get(v) - 1);
                }
                res[i][j] = cur.lastKey();
            }
        }
        return res;
    }
}
