package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LC902 {

    @Test
    public void test() {
        String[] digits = new String[]{"5", "7", "8"};
        int n = 59;
        System.out.println(atMostNGivenDigitSet(digits, n));
    }

    public int atMostNGivenDigitSet(String[] digits, int n) {
        int[] d = new int[digits.length];
        for (int i = 0; i < d.length; i++) d[i] = Integer.parseInt(digits[i]);
        List<Integer> l = new ArrayList();
        while (n > 0) {
            l.add(0, n % 10);
            n = n / 10;
        }
        int freeDigits = l.size() - 1;
        int res = 0, p = 1;
        for (int i = 1; i <= freeDigits; i++) {
            res += p * d.length;
            p = p * d.length;
        }
        int i = 0;
        while (i < l.size()) {
            int cur = l.get(i);
            int index = Arrays.binarySearch(d, cur);
            if (index < 0) {
                index = -index - 1;
                res += index * Math.pow(d.length, l.size() - i - 1);
                return res;
            }
            if (i == l.size() - 1) {
                res += index + 1;
            } else {
                res += index * Math.pow(d.length, l.size() - i - 1);
            }
            i++;
        }
        return res;
    }
}
