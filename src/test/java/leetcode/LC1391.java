package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LC1391 {

    @Test
    public void test() {
        int[][] grid = new int[][]{{2,4,3},{6,5,2}};
        System.out.println(hasValidPath(grid));
    }

    int m;
    int n;
    int[][] dirs = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    // value is directions. 0 is up, 1 is right, 2 is down, 3 is left
    Map<Integer, List<Integer>> map = new HashMap();
    public boolean hasValidPath(int[][] grid) {
        this.m = grid.length;
        this.n = grid[0].length;
        map.put(1, Arrays.asList(1, 3));
        map.put(2, Arrays.asList(0, 2));
        map.put(3, Arrays.asList(3, 2));
        map.put(4, Arrays.asList(1, 2));
        map.put(5, Arrays.asList(0, 3));
        map.put(6, Arrays.asList(0, 1));
        return dfs(grid, 0, 0, map.get(grid[0][0]).get(0));
    }

    private boolean dfs(int[][] g, int r, int c, int need) {
        if (r < 0 || r >= m || c < 0 || c >= n) return false;

        if (g[r][c] < 0) return false;
        if (!map.get(g[r][c]).contains(need)) return false;
        if (r == m - 1 && c == n - 1) return true;
        int cur = g[r][c];
        g[r][c] = -g[r][c];
        for (int i = 0; i < 2; i++) {
            int k = map.get(cur).get(i);
            int[] d = dirs[k];
            if (dfs(g, r + d[0], c + d[1], getNeed(k))) return true;
        }
        return false;
    }

    private int getNeed(int k) {
        switch (k) {
            case 0:
                return 2;
            case 2:
                return 0;
            case 1:
                return 3;
            case 3:
                return 1;
        }
        return -1;
    }
}
