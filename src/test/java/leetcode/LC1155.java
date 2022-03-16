package leetcode;

import org.junit.Test;

public class LC1155 {
    @Test
    public void test() {
        int d = 2;
        int f = 6;
        int target = 12;
        int numRollsToTarget = numRollsToTarget(d, f, target);
    }

    public int numRollsToTarget(int d, int f, int target) {
        int mod = (int)1e9 + 7;
        int[][] dp = new int[d + 1][target + 1];
        for (int i = 1; i <= target; i++) dp[1][i] = 1;
        for (int i = 1; i < d; i++) {
            for (int j = 1; j <= Math.min(f, target); j++) {
                for (int k = 1; k + j <= target && k <= f; k++) {
                    dp[i + 1][k + j] = (dp[i + 1][k + j] + dp[i][j]) % mod;
                }
            }
        }
        return dp[d][target];
    }
}
