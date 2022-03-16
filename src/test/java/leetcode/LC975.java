package leetcode;

import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;

public class LC975 {

    @Test
    public void test() {
//        int[] arr = new int[]{1,2,3,2,1,4,4,5};
        int[] arr = new int[]{1,2,3,2,4};
        System.out.println(oddEvenJumps(arr));
    }

    public int oddEvenJumps(int[] arr) {
        int n = arr.length;
        int[] nextGt = new int[n];
        Arrays.fill(nextGt, -1);
        int[] nextSt = new int[n];
        Arrays.fill(nextSt, -1);
        Stack<Integer> ngs = new Stack();
        Stack<Integer> nss = new Stack();
        for (int i = 0; i < n; i++) {
            while (!ngs.isEmpty() && arr[ngs.peek()] <= arr[i]) {
                nextGt[ngs.pop()] = i;
            }
            ngs.push(i);

            while (!nss.isEmpty() && arr[nss.peek()] >= arr[i]) {
                nextSt[nss.pop()] = i;
            }
            nss.push(i);
        }

        int[] oddReach = new int[n];
        int[] evenReach = new int[n];
        Arrays.fill(evenReach, 1);

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nextGt[j] == i) {
                    oddReach[i] += evenReach[j];
                }
            }

            for (int j = 0; j < i; j++) {
                if (nextSt[j] == i) {
                    evenReach[i] += oddReach[j];
                }
            }
        }
        return oddReach[n - 1] + evenReach[n - 1];
    }
}
