package leetcode;

import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class LC934 {

    @Test
    public void test() {
        int[][] a = new int[][]{{0,1},{1,0}};
        shortestBridge(a);
    }

    int n;
    int[][] dirs = new int[][]{{-1, 0}, {0, 1}, {0, -1}, {1, 0}};
    Queue<int[]> q = new LinkedList();
    Set<String> set = new HashSet();
    public int shortestBridge(int[][] a) {
        n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 1) {
                    dfs(a, i, j);
                    break;
                }
            }
        }

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1], d = cur[2];
            if (a[r][c] == 1) return d;
            for (int[] dir : dirs) {
                int nr = r + dir[0], nc = c + dir[1];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n && !set.contains(nr + " " + nc)) {
                    set.add(nr + " " + nc);
                    q.offer(new int[]{nr, nc, d + 1});
                }
            }
        }
        return -1;
    }

    private void dfs(int[][] a, int r, int c) {
        if (r < 0 || r >= n || c < 0 || c >= n) return;
        if (a[r][c] != 1) return;
        String k = r + " " + c;
        if (set.contains(k)) return;

        set.add(k);
        q.offer(new int[]{r, c, 0});
        a[r][c] = -1;
        for (int[] d : dirs) {
            dfs(a, r + d[0], c + d[1]);
        }
    }
}
