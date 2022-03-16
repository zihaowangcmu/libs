package leetcode;

import org.junit.Test;

import java.util.Arrays;

public class LC375 {

    @Test
    public void test() {
        int n = 10;
        int amount = getMoneyAmount(n);
        System.out.println(amount);
    }

    public int getMoneyAmount(int n) {
        int[][] dp = new int[n + 1][n + 1];
        for (int i = 0; i < n + 1; i++) Arrays.fill(dp[i], Integer.MAX_VALUE);
        for (int i = 0; i < n + 1; i++) dp[i][i] = 0;
        // 注意d在最外面 通过分析transfer equation得到
        for (int d = 1; d < n; d++) {
            for (int i = 1; i + d < n + 1; i++) {
                int j = i + d;
                for (int k = i; k < j + 1; k++) {
                    dp[i][j] = Math.min(dp[i][j], k + Math.max(k - 1 >= i ? dp[i][k - 1] : 0, k + 1 <= j ? dp[k + 1][j] : 0));
                }
            }
        }
        return dp[1][n];
    }
}
