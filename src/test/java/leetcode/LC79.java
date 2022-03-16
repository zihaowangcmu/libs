package leetcode;

import org.junit.Test;

public class LC79 {
    @Test
    public void test() {
        char[][] a = new char[][]{{'a', 'b', 'c'}};
        String w = "abc";
        exist(a, w);
    }

    char[][] b;
    int m;
    int n;
    String w;
    int[][] dirs = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public boolean exist(char[][] board, String word) {
        this.b = board;
        this.w = word;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dfs(0, i, j, new boolean[m][n])) return true;
            }
        }
        return false;
    }

    private boolean dfs(int cur, int r, int c, boolean[][] seen) {
        if (cur == w.length()) return true;
        if (r < 0 || r >= m || c < 0 || c >= n || seen[r][c] || b[r][c] != w.charAt(cur)) return false;
        // if (b[r][c] != w.charAt(cur)) return false;
        seen[r][c] = true;
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (dfs(cur + 1, nr, nc, seen)) return true;
        }
        seen[r][c] = false;
        return false;
    }
}