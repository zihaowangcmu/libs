package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LC18 {

    @Test
    public void test() {
        int[] nums = new int[]{0,0,1,2,-1,-2};
        int target = 0;
        List<List<Integer>> lists = fourSum(nums, target);
    }

    public List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> list = new ArrayList();
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int t = target - nums[i];
            for (int j = i + 1; j < n - 1; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) continue;
                int l = j + 1;
                int r = n - 1;
                while (l < r) {
                    if (l > j + 1 && nums[l] == nums[l - 1]) {
                        l++;
                        continue;
                    }
                    if (r < n - 1 && nums[r] == nums[r + 1]) {
                        r--;
                        continue;
                    }
                    int sum = nums[l] + nums[r] + nums[j];
                    if (sum == t) {
                        List<Integer> li = Arrays.asList(nums[i], nums[l], nums[j], nums[r]);
                        list.add(li);
                        l++;
                        r--;
                    } else if (sum < t) {
                        l++;
                    } else {
                        r--;
                    }
                }
            }
        }
        return list;
    }
}
