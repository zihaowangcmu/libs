package leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LC1625 {

    @Test
    public void test() {
        String s = "74";
        int a = 5, b = 1;
        System.out.println(findLexSmallestString(s, a, b));
    }

    Map<Integer, Integer> map = new HashMap();
    String res = null;
    public String findLexSmallestString(String s, int a, int b) {
        int n = s.length();

        for (int i = 0; i <= 9; i++) {
            boolean[] v = new boolean[10];
            int cur = i;
            int min = cur;
            while (!v[cur]) {
                v[cur] = true;
                min = Math.min(cur, min);
                cur = (cur + a) % 10;
            }
            map.put(i, min);
        }

        int[] num = new int[n];
        for (int i = 0; i < n; i++) {
            num[i] = s.charAt(i) - '0';
        }
        boolean[] visited = new boolean[n];
        dfs(num, 0, visited, b);
        return res;
    }

    private void dfs(int[] nums, int index, boolean[] visited, int b) {
        if (visited[index]) return;

        visited[index] = true;
        int add = 0;
        if (index % 2 != 0) {
            add = map.get(nums[index]) - nums[index];
            if (add < 0) add += 10;
        } else {
            add = map.get(nums[(index + 1) % 10]) - nums[(index + 1) % 10];
            if (add < 0) add += 10;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = index; i < index + nums.length; i++) {
            int j = i % nums.length;
            sb.append((nums[j] + (j % 2 == 0 ? 0 : add)) % 10 + "");
        }
        String r = sb.toString();
        if (res == null || r.compareTo(res) < 0) {
            res = r;
        }
        dfs(nums, (index + b) % nums.length, visited, b);
    }
}
