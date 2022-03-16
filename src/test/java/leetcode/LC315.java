package leetcode;

import io.vertx.core.json.Json;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LC315 {

    /**
     * Count how many nums are smaller after itself
     */
    @Test
    public void test1() {
        int[] a = new int[]{5, 2, 6, 1};
        List<Integer> res = countSmallerAfter(a);
    }

    class Pair {
        int index;
        int val;
        public Pair(int index, int val) {
            this.index = index;
            this.val = val;
        }
    }
    public List<Integer> countSmallerAfter(int[] nums) {
        List<Integer> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        Pair[] arr = new Pair[nums.length];
        Integer[] smaller = new Integer[nums.length];
        Arrays.fill(smaller, 0);
        for (int i = 0; i < nums.length; i++) {
            arr[i] = new Pair(i, nums[i]);
        }
        mergeSort(arr, smaller);
        res.addAll(Arrays.asList(smaller));
        return res;
    }
    private Pair[] mergeSort(Pair[] arr, Integer[] smaller) {
        if (arr.length <= 1) {
            return arr;
        }
        int mid = arr.length / 2;
        Pair[] left = mergeSort(Arrays.copyOfRange(arr, 0, mid), smaller);
        Pair[] right = mergeSort(Arrays.copyOfRange(arr, mid, arr.length), smaller);
        for (int i = 0, j = 0; i < left.length || j < right.length;) {
            if (j == right.length || i < left.length && left[i].val <= right[j].val) {
                arr[i + j] = left[i];
                smaller[left[i].index] += j;
                i++;
            } else {
                arr[i + j] = right[j];
                j++;
            }
        }
        return arr;
    }




    /**
     * Count how many nums are smaller before itself
     */
    @Test
    public void test2() {
        int[] a = new int[]{5, 2, 6, 1};
        List<Integer> res = countSmallerBefore(a);
        System.out.println(Json.encode(res));
    }


    public List<Integer> countSmallerBefore(int[] nums) {
        int n = nums.length;
        int[][] pairs = new int[n][2];
        // [val, original index]
        for (int i = 0; i < n; i++) pairs[i] = new int[]{nums[i], i};
        int[] res = new int[n];
        mergeSort(pairs, res);
        List<Integer> ret = new ArrayList();
        for (int i : res) ret.add(i);
        return ret;
    }

    private int[][] mergeSort(int[][] nums, int[] res) {
        if (nums.length <= 1) return nums;
        // mid of nums
        int mid = nums.length / 2;
        int[][] left = mergeSort(Arrays.copyOfRange(nums, 0, mid), res);
        int[][] right = mergeSort(Arrays.copyOfRange(nums, mid, nums.length), res);
        // l: ptr of left, r: ptr of right
        int l = 0, r = 0, i = 0;
        while (l + r < nums.length) {
            if (l == left.length || r < right.length && right[r][0] > left[l][0]) {
                res[right[r][1]] += left.length - l;
                nums[i++] = right[r++];
            } else {
                nums[i++] = left[l++];
            }
        }
        return nums;
    }
}
