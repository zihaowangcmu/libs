package leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LC697 {

    @Test
    public void test() {
        int[] nums = new int[]{1,2,2,3,1,4,2};
        int res = findShortestSubArray(nums);
        System.out.println(res);
    }

    public int findShortestSubArray(int[] nums) {
        Map<Integer, Integer> freq = new HashMap();
        Map<Integer, int[]> pos = new HashMap();
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            int cur = nums[i];
            freq.putIfAbsent(cur, 0);
            freq.put(cur, freq.get(cur) + 1);
            max = Math.max(max, freq.get(cur));
            pos.putIfAbsent(cur, new int[]{i, i});
            pos.get(cur)[1] = i;
        }

        int min = nums.length;
        for (int k : freq.keySet()) {
            if (max == freq.get(k)) {
                min = Math.min(min, pos.get(k)[1] - pos.get(k)[0]);
            }
        }
        return min + 1;
    }
}
