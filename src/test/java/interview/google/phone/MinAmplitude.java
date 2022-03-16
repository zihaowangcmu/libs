package interview.google.phone;

import org.junit.Test;

import java.util.Arrays;

/**
 * https://leetcode.com/discuss/interview-question/553399/
 * Q1
 *
 * 第一题：给一个array有N个数字，每一次选一个array里的element替换他的一个数，最多执行三次。然后求最小的amplitude
 * 栗子1， A = [-9, 8 -1]    -9 和 8 替换成 -1， amplitude 是 0；
 * 栗子2， A = [14,10,5,1,0] 把 14,10,5 替换成 0 or 1. amplitude 是 1；
 * amplitude: max - min
 */
public class MinAmplitude {

    @Test
    public void test() {
        int[] nums = new int[]{14, 10, 5, 1 ,0};
        solve(nums);
    }

    private int solve(int[] nums) {
        if (nums.length < 5) return 0;
        Arrays.sort(nums);
        int n = nums.length;
        int l = 0, r = n - 4;
        int res = nums[n - 1] - nums[0];
        for (; r < n; l++, r++) {
            res = Math.min(res, nums[r] - nums[l]);
        }
        return res;
    }
}
