package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LC880 {

    @Test
    public void test() {
        String s = "ha22";
        int k = 5;
        System.out.println(decodeAtIndex(s, k));
    }

    public String decodeAtIndex(String s, int k) {
        List<Integer>[] nums = new ArrayList[26];
        for (int i = 0; i < 26; i++) nums[i] = new ArrayList();
        int size = 0, cur = 0;
        k--;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) {
                nums[c - 'a'].add(cur);
                if (cur == k) return c + "";
                cur++;
                size++;
            } else {
                int repeat = Integer.parseInt(c + "") - 1;
                for (int j = 0; j < 26; j++) {
                    int cs = nums[j].size();
                    for (int m = 0; m < cs; m++) {
                        for (int r = 1; r < repeat + 1; r++) {
                            nums[j].add(nums[j].get(m) + size * r);
                            if (cur == k) return ((char) (j + 'a')) + "";
                            cur++;
                        }

                    }
                }
                size *= (repeat + 1);
            }
        }
        return "";
    }
}
