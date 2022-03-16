package algorithm.sort;

import org.junit.Test;

public class QuickSort {

    @Test
    public void test() {
        int[] nums = new int[]{10, 9, 8, 1, 3, 6, 5, 3, 3, 2, 1, 13, 15};
        quickSort(nums);
    }

    private void quickSort(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
    }

    private void quickSort(int[] nums, int l, int r) {
        if (l >= r) return;

        int pivotIndex = partition(nums, l, r);
        quickSort(nums, l, pivotIndex - 1);
        quickSort(nums, pivotIndex + 1, r);
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
