package leetcode;

import org.junit.Test;

import java.util.Arrays;

public class LC1055 {

    @Test
    public void test() {
        String source = "aa";
        String target = "aaaaaa";
        int shortestWay = shortestWay(source, target);
    }

    public int shortestWay(String a, String b) {
        int m = a.length() + 1, n = b.length() + 1;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i < n; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (dp[j] == Integer.MAX_VALUE) continue;
                if (dp[j] >= dp[i]) continue;
                if (!sub(a, b.substring(j, i))) continue;
                dp[i] = Math.min(dp[i], dp[j] + 1);
            }
        }
        return dp[n - 1] == Integer.MAX_VALUE ? -1 : dp[n - 1];
    }

    private boolean sub(String a, String b) {
        int p = 0;
        for (char c : b.toCharArray()) {
            p = a.indexOf(c, p);
            if (p < 0) return false;
            p++;
        }
        return true;
    }
}
