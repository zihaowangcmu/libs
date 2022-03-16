package leetcode;

import org.junit.Test;

public class LC312 {

    @Test
    public void test() {
        int[] nums = new int[]{3,1,5,8};
        System.out.println(maxCoins(nums));
    }

    int[] coins;
    int[][] memo;
    public int maxCoins(int[] nums) {
        int n = nums.length;
        coins = new int[n + 2];
        int j = 1;
        for (int i = 0; i < n; i++) {
            if (nums[i] > 0) {
                // remove ballons value == 0
                coins[j++] = nums[i];
            }
        }
        coins[0] = coins[j] = 1;
        this.memo = new int[j + 1][j + 1];
        return dfs(0, j);
    }

    // The max coins to get when burst the LAST ballon and the left and right is given
    private int dfs(int l, int r) {
        if (l + 1 == r) return 0;
        if (memo[l][r] != 0) return memo[l][r];
        int res = 0;
        for (int i = l + 1; i < r; i++) {
            res = Math.max(res, coins[i - 1] * coins[i] * coins[i + 1] + dfs(l, i) + dfs(i, r));
        }
        return memo[l][r] = res;
    }
}
