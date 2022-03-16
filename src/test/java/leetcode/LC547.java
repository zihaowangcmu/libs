package leetcode;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class LC547 {

    @Test
    public void test() {
        int[][] isConnected = new int[][]{{1,1,0},{1,1,0},{0,0,1}};
        findCircleNum(isConnected);
    }

    int n;
    Set<Integer> visited;
    public int findCircleNum(int[][] isConnected) {
        visited = new HashSet();
        n = isConnected.length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            if (!visited.contains(i)) {
                dfs(isConnected, i);
                res++;
            }
        }
        return res;
    }

    private void dfs(int[][] graph, int cur) {
        if (visited.contains(cur)) return;

        visited.add(cur);
        for (int i = 0; i < n; i++) {
            if (graph[cur][i] == 1) {
                dfs(graph, i);
            }
        }
    }
}
