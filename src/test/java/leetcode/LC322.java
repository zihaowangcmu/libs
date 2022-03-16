package leetcode;

import org.junit.Test;

/**
 * complete knapsack
 * 完全背包
 */
public class LC322 {

    @Test
    public void test() {
        int[] coins = new int[]{1, 2, 5};
        int amount = 11;
        int coinChange = coinChange(coins, amount);
        System.out.println(coinChange);
    }

    public int coinChange(int[] coins, int amount) {
        int m = coins.length + 1, n = amount + 1;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int i = 1; i < m; i++) {
            int coin = coins[i - 1];
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i][j], dp[i - 1][j]);
                // NOTE: here we use dp[i][j - h], because this is complete knapsack, so
                // if you can use an element more than 1 time.
                // if you use dp[i - 1][j - h], it means you are using item i at most 1 time!
                if (j - coin >= 0 && dp[i][j - coin] != Integer.MAX_VALUE) {
                        dp[i][j] = Math.min(dp[i][j], dp[i][j - coin] + 1);
                }
            }
        }
        return dp[m - 1][n - 1] == Integer.MAX_VALUE ? -1 : dp[m - 1][n - 1];
    }
}
