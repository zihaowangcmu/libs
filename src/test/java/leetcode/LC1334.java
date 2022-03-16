package leetcode;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class LC1334 {

    @Test
    public void test() {
        int n = 4;
        int t = 4;
        int[][] edges = new int[][]{{0,1,3}, {1,2,1}, {1,3,4}, {2,3,1}};
        System.out.println(findTheCity(n, edges, t));
    }

    public int findTheCity(int n, int[][] edges, int t) {
        Map<Integer, Integer>[] g = new HashMap[n];
        for (int i = 0; i < n; i++) g[i] = new HashMap();
        for (int[] e : edges) {
            g[e[0]].put(e[1], e[2]);
            g[e[1]].put(e[0], e[2]);
        }

        int[][] dist = new int[n][n];
        for (int[] d : dist) Arrays.fill(d, Integer.MAX_VALUE);
        for (int i = 0; i < n; i++) dist[i][i] = 0;

        for (int i = 0; i < n; i++) {
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> (a[1] - b[1]));
            pq.offer(new int[]{i, 0});
            while (!pq.isEmpty()) {
                int[] cur = pq.poll();
                int node = cur[0], weight = cur[1];
                for (int nei : g[node].keySet()) {
                    int nw = weight + g[node].get(nei);
                    if (nw < dist[i][nei]) {
                        dist[i][nei] = nw;
                        dist[nei][i] = nw;
                        pq.offer(new int[]{nei, nw});
                    }
                }
            }
        }

        int min = Integer.MAX_VALUE, ret = -1;
        for (int i = 0; i < n; i++) {
            int cur = 0;
            for (int j = 0; j < n; j++) {
                if (i != j && dist[i][j] <= t) {
                    cur++;
                    if (cur > min) break;
                }
            }
            if (cur <= min) {
                min = cur;
                ret = i;
            }
        }
        return ret;
    }
}
