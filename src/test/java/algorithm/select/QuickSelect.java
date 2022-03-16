package algorithm.select;

import org.junit.Test;

/**
 * Quickselect is a selection algorithm to find the k-th smallest element in an unordered list. It is related to the quick sort sorting algorithm.
 *
 * The algorithm is similar to QuickSort.
 * The difference is, instead of recurring for both sides (after finding pivot),
 * it recurs only for the part that contains the k-th smallest element.
 * The logic is simple, if index of partitioned element is more than k, then we recur for left part.
 * If index is same as k, we have found the k-th smallest element and we return.
 * If index is less than k, then we recur for right part.
 * This reduces the expected complexity from O(n log n) to O(n), with a worst case of O(n^2).
 */
public class QuickSelect {

    @Test
    public void test() {
        int[] nums = new int[]{10, 9, 8, 1, 3, 6, 5, 3, 3, 2, 1, 13, 15};
        // k starts from 1.
        int k = 2;
        int res = quickSelect(nums, k);
    }

    private int quickSelect(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length - 1, k);
    }

//    // method 1: k changes and compare with l.
//    private int quickSelect(int[] nums, int l, int r, int k) {
//        int pivotIndex = partition(nums, l, r);
//        if (pivotIndex - l + 1 == k) return nums[pivotIndex];
//        if (pivotIndex - l + 1 < k) return quickSelect(nums, pivotIndex + 1, r, k - (pivotIndex - l + 1));
//        return quickSelect(nums, l, pivotIndex - 1, k);
//    }

    // method 2: k does not change and compare with 0.
    private int quickSelect(int[] nums, int l, int r, int k) {
        int pivotIndex = partition(nums, l, r);
        if (pivotIndex + 1 == k) return nums[pivotIndex];
        if (pivotIndex + 1 < k) return quickSelect(nums, pivotIndex + 1, r, k);
        return quickSelect(nums, l, pivotIndex - 1, k);
    }

    // Here we pick last element as pivot.
    // We can use others as pivot as well.
    private int partition(int[] nums, int l, int r) {
        int i = l, j = l;
        int pivot = nums[r];
        for (; j < r; j++) {
            if (nums[j] < pivot) {
                swap(nums, i++, j);
            }
        }
        swap(nums, i, r);
        return i;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
