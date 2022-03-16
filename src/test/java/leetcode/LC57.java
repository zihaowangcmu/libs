package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LC57 {

    @Test
    public void test() {
        String S = "1-401--349---90--88";
        TreeNode res = recoverFromPreorder(S);
        System.out.println(res.val);
    }

    public TreeNode recoverFromPreorder(String S) {
        List<Integer> preorder = new ArrayList();
        List<Integer> depth = new ArrayList();
        int p = 0, m = 0;
        while (m < S.length()) {
            char c = S.charAt(m);
            if (c == '-') {
                m++;
            } else {
                int n = m;
                while (m < S.length() && S.charAt(m) != '-') m++;
                preorder.add(Integer.parseInt(S.substring(n, m)));
                depth.add(n - p);
                p = m;
            }
        }
        return null;
    }
}
