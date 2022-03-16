package leetcode;

import org.junit.Test;

import java.util.Arrays;

/**
 * 完全背包
 * CompletePack
 */
public class LC518 {

    @Test
    public void test() {
        int amount = 5;
        int[] coins = new int[]{1, 2, 5};
//        int change = change1(amount, coins);
        int change = change2(amount, coins);
        System.out.println(change);
    }

    public int change1(int amount, int[] coins) {
        int m = coins.length + 1, n = amount + 1;
        int[][] dp = new int[m][n];
        for (int i = 1; i < m; i++) dp[i][0] = 1;
        for (int i = 1; i < m; i++) {
            int coin = coins[i - 1];
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + (j - coin >= 0 ? dp[i][j - coin] : 0);
            }
        }
        return dp[m - 1][n - 1];
    }

    // compress to 1D
    public int change2(int amount, int[] coins) {
        int n = amount + 1;
        int[] dp = new int[n];
        dp[0] = 1;
        for (int coin : coins) {
            for (int j = 0; j < n; j++) {
                dp[j] = dp[j] + (j - coin >= 0 ? dp[j - coin] : 0);
            }
        }
        return dp[n - 1];
    }
}
