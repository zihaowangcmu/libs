package leetcode;

import org.junit.Test;

import java.util.Stack;

public class LC536 {

    @Test
    public void test() {
//        String s = "-4(2(3)(1))(6(5)(7))";
        String s = "4";
        TreeNode treeNode = str2tree(s);
    }

    public TreeNode str2tree(String str) {
        Stack<TreeNode> s = new Stack();
        int l = 0, r = 0;
        for (; l < str.length(); l++) {
            char c = str.charAt(l);
            if (c == '-' || Character.isDigit(c)) {
                r = l;
                while (r < str.length() && (str.charAt(r) == '-' || Character.isDigit(str.charAt(r)))) r++;
                int v = Integer.parseInt(str.substring(l, r));
                TreeNode cur = new TreeNode(v);
                if (!s.isEmpty()) {
                    TreeNode p = s.peek();
                    if (p.left == null)
                        p.left = cur;
                    else
                        p.right = cur;
                }
                s.push(cur);
                l = r - 1;
            } else if (c == ')') {
                s.pop();
            }
        }
        return s.pop();
    }
}
