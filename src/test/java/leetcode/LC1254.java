package leetcode;

import org.junit.Test;

public class LC1254 {

    @Test
    public void test() {
        int[][] grid = new int[][]{{0,0}};
        System.out.println(closedIsland(grid));
    }

    int m;
    int n;
    int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
    public int closedIsland(int[][] grid) {
        int res = 0;
        m = grid.length;
        n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    boolean b = dfs(grid, i, j);
                    if (b) res++;
                    // if (dfs(grid, i, j)) res++;
                }
            }
        }
        return res;
    }

    private boolean dfs(int[][] grid, int r, int c) {
        if (r < 0 || r >= m || c < 0 || c >= n) return false;
        if (grid[r][c] != 0) return true;

        grid[r][c] = -1;
        boolean res = true;
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            // do not break earlier so that it updates grid for all connected lands
            res = dfs(grid, nr, nc) && res;
        }
        return res;
    }
}
