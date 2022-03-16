package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LC1301 {

    @Test
    public void test() {
        String[] strings = new String[]{"E23","2X2","12S"};
        List<String> board = new ArrayList<>();
        for (String s : strings) board.add(s);
        pathsWithMaxScore(board);
    }

    public int[] pathsWithMaxScore(List<String> board) {
        int n = board.size();
        int[][] b = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char c = board.get(i).charAt(j);
                if (c == 'E' || c == 'S') b[i][j] = 0;
                else if (c == 'X') b[i][j] = -1;
                else b[i][j] = c - '0';
            }
        }

        Map<Integer, Integer>[][] dp = new HashMap[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = new HashMap();
            }
        }
        dp[n - 1][n - 1].put(0, 1);

        int p = 0;
        for (int i = n - 2; i >= 0; i--) {
            int cur = b[i][n - 1];
            if (cur == -1) {
                break;
            }
            p += cur;
            dp[i][n - 1].put(p, 1);
        }

        p = 0;
        for (int j = n - 2; j >= 0; j--) {
            int cur = b[n - 1][j];
            if (cur == -1) {
                break;
            }
            p += cur;
            dp[n - 1][j].put(p, 1);
        }

        int[][] dirs = new int[][]{{0, 1}, {1, 0}, {1, 1}};
        int max = 0;
        for (int d = n - 2; d >= 0; d--) {
            // scan row right to left
            for (int j = d; j >= 0; j--) {
                int cur = b[d][j];
                if (cur == -1) continue;
                for (int[] dir : dirs) {
                    int nr = d + dir[0], nc = j + dir[1];
                    if (b[nr][nc] == -1) continue;
                    for (int k : dp[nr][nc].keySet()) {
                        int newSum = k + cur;
                        max = Math.max(max, newSum);
                        dp[d][j].putIfAbsent(newSum, 0);
                        dp[d][j].put(newSum, dp[d][j].get(newSum) + dp[nr][nc].get(k));
                    }
                }
            }
            // scan col bottom to top
            for (int i = d - 1; i >= 0; i--) {
                int cur = b[i][d];
                if (cur == -1) continue;
                for (int[] dir : dirs) {
                    int nr = i + dir[0], nc = d + dir[1];
                    if (b[nr][nc] == -1) continue;
                    for (int k : dp[nr][nc].keySet()) {
                        int newSum = k + cur;
                        max = Math.max(max, newSum);
                        dp[i][d].putIfAbsent(newSum, 0);
                        dp[i][d].put(newSum, dp[i][d].get(newSum) + dp[nr][nc].get(k));
                    }
                }
            }
        }

        return new int[]{max, dp[0][0].get(max)};
    }
}
