package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LC894 {

     @Test
     public void test() {
         allPossibleFBT(5);
     }

    public List<TreeNode> allPossibleFBT(int N) {
        if (N % 2 == 0) return new ArrayList();
        List<NodeAndLeaves> l = build(N);
        List<TreeNode> res = new ArrayList();
        for (NodeAndLeaves nal : l) res.add(nal.node);
        return res;
    }

    private List<NodeAndLeaves> build(int n) {
        List<NodeAndLeaves> res = new ArrayList();
        if (n == 1) {
            List<TreeNode> leaves = new ArrayList();
            TreeNode root = new TreeNode(0);
            leaves.add(root);
            res.add(new NodeAndLeaves(root, leaves));
            return res;
        }

        List<NodeAndLeaves> next = build(n - 2);
        for (NodeAndLeaves nal : next) {
            for (int i = 0; i < nal.leaves.size(); i++) {
                TreeNode leaf = nal.leaves.get(i);
                leaf.left = new TreeNode(0);
                leaf.right = new TreeNode(0);
                if (has(res, nal.node)) continue;
                List<TreeNode> l = new ArrayList();
                l.addAll(nal.leaves);
                l.remove(i);
                l.add(i, leaf.right);
                l.add(i, leaf.left);
                res.add(new NodeAndLeaves(nal.node, l));
            }
        }
        return res;
    }

    private boolean has(List<NodeAndLeaves> l, TreeNode n) {
        for (NodeAndLeaves nal : l) {
            if (isSame(nal.node, n)) return true;
        }
        return false;
    }

    private boolean isSame(TreeNode n1, TreeNode n2) {
        if (n1 == null && n2 == null) return true;
        if (n1 == null || n2 == null) return false;
        return isSame(n1.left, n2.left) && isSame(n1.right, n2.right);
    }

    class NodeAndLeaves {
        public TreeNode node;
        public List<TreeNode> leaves;
        public NodeAndLeaves(TreeNode node, List<TreeNode> leaves) {
            this.node = node;
            this.leaves = leaves;
        }
    }
}
