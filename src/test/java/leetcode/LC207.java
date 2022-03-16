package leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LC207 {

    @Test
    public void test() {
        int n = 4;
        int[][] p = new int[][]{{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        boolean canFinish = canFinish(n, p);
    }

    Map<Integer, Set<Integer>> map;
    Map<Integer, Boolean> memo = new HashMap();
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        map = new HashMap();

        for (int[] p : prerequisites) {
            int c1 = p[0], c2 = p[1];
            map.computeIfAbsent(c2, v -> new HashSet()).add(c1);
        }

        for (int i = 0; i < numCourses; i++) {
            dfs(i, new HashSet());
            if (!memo.get(i)) return false;
        }
        return true;
    }

    private boolean dfs(int cur, Set<Integer> visited) {
        if (memo.containsKey(cur)) return memo.get(cur);
        if (visited.contains(cur)) return false;
        if (!map.containsKey(cur)) {
            memo.put(cur, true);
            return true;
        }
        visited.add(cur);
        for (int k : map.get(cur)) {
            if (!dfs(k, visited)) {
                memo.put(cur, false);
                visited.remove(cur);
                return false;
            }
        }
        visited.remove(cur);
        memo.put(cur, true);
        return true;
    }
}
