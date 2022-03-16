package interview.google.onsite;

import org.junit.Test;

/**
 * 多重背包
 * Multiple knapsack
 * 参考 https://leetcode-cn.com/circle/article/2ZcRi7/
 *
 * 有很多乐高，比如大小为1,2， 3， 5的乐高，给一个target，比如10，求问最少的乐高块数拼凑得到target.
 * 比如用两个大小为5的乐高可以得到target 10，也可以用10个大小为1的乐高得到target 10，取数量最小的，就是两个5
 * 乐高不是无限供给，给定数量，比如1:10， 2:5， 3:4， 5:1， 就是说，大小为1的乐高有10片，大小为2的乐高有5片。。。。。
 * 大小为5的乐高有一片，那么最终答案可以是 一片5+一片3+一片2，总共用了3片乐高
 */
public class Lego {

    @Test
    public void test() {
        int target = 10;
        int[] heights = new int[]{1, 2, 3, 5};
        // how many legos of this height
        int[] nums    = new int[]{10, 5, 4, 1};
        // if the nums is all 1, then iit turns out to be 0/1 knapsack
//        int[] nums    = new int[]{1, 1, 1, 1};
        int res = lego2(heights, nums, target);
        System.out.println(res);
    }

    /**
     * 转化为0/1背包
     * @param heights
     * @param nums
     * @param target
     * @return
     */
    public int lego1(int[] heights, int[] nums, int target) {
        int m = 0;
        for (int i = 0; i < heights.length; i++) m += nums[i];
        m = m + 1;
        int n = target + 1;
        int[] newHeights = new int[m - 1];
        int p = 0;
        for (int i = 0; i < heights.length; i++) {
            for (int j = 1; j <= nums[i]; j++) {
                newHeights[p++] = heights[i];
            }
        }

        int[][] dp = new int[m][n];
        // dp[0 ~ n - 1][0] == 0
        // others are MAX
        for (int i = 0; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int i = 1; i < m; i++) {
            int h = newHeights[i - 1];
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i][j], dp[i - 1][j]);
                // NOTE: here we use dp[i - 1][j - h], because this is 0/1 knapsack, so
                // if you can not use an element more than 1 time.
                // if you use dp[i][j - h], it means you are using item i more than 1 time!
                if (j - h >= 0 && dp[i - 1][j - h] != Integer.MAX_VALUE) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - h] + 1);
                }
            }
        }
        return dp[m - 1][n - 1] == Integer.MAX_VALUE ? -1 : dp[m - 1][n - 1];
    }

    private int lego2(int[] heights, int[] nums, int target) {
        int m = heights.length + 1, n = target + 1;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int i = 1; i < m; i++) {
            int h = heights[i - 1];
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i][j], dp[i - 1][j]);
                for (int k = 1;
                     // NOTE: we are using dp[i - 1][j - k * h]
                     // because it is similar to 0/1 knapsack, if you use dp[i][j - k * h]
                     // it means you are actually using it indefinitely
                     k <= nums[i - 1] && j - k * h >= 0 && dp[i - 1][j - k * h] != Integer.MAX_VALUE;
                     k++) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - k * h] + k);
                }
            }
        }
        return dp[m - 1][n - 1] == Integer.MAX_VALUE ? -1 : dp[m - 1][n - 1];
    }
}
