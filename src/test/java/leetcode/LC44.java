package leetcode;

import org.junit.Test;

public class LC44 {

    @Test
    public void test() {
        String s = "aa";
        String p = "*";
        boolean match = isMatch(s, p);
    }

    public boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int i = 1; i < n + 1; i++) {
            dp[0][i] = p.charAt(i - 1) == '*' ? dp[0][i - 1] : false;
        }
        for (int i = 1; i <= m; i++) {
            char cs = s.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                char cp = p.charAt(j - 1);
                if (cs == cp) dp[i][j] = dp[i - 1][j - 1];
                else if (cp == '?') {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (cp == '*') {
                    for (int k = 0; k <= i; k++) {
                        dp[i][j] = dp[k][j - 1];
                        if (dp[i][j]) break;
                    }
                } // else if false
            }
        }
        return dp[m][n];
    }
}
