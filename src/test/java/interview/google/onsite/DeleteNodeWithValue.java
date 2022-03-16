package interview.google.onsite;

import leetcode.TreeNode;
import org.junit.Test;

/**
 * 一个树形结构，有两种类型的节点。要求删除所有的type b节点，并保留所有的type a节点。
 * type b节点下面如果有type a节点，需要移动到最近的parent type a节点下面。
 * 这个题也是dfs。
 * 我刚开始走错方向了，写了一个需要使用parent pointer的方案。面试官一开始不理解，我给他解释了半天。他说可以，但是时间复杂度比较高。
 * 最后10分钟，一起写了一个递归方案，也是面试官认可的方案。最后简单讨论了下空间时间复杂度。
 * 这轮估计是weak hire。因为面试官帮忙才想出来最佳方案。
 */
public class DeleteNodeWithValue {

    /**
     * Delete all the nodes with value 'target' from a binary tree
     */
    @Test
    public void test() {
        /**
         *     0
         *  1    2
         * 3 0  0 4
         */
        TreeNode a = new TreeNode(3);
        TreeNode b = new TreeNode(0);
        TreeNode c = new TreeNode(0);
        TreeNode d = new TreeNode(4);
        TreeNode e = new TreeNode(1, a, b);
        TreeNode f = new TreeNode(2, c, d);
        TreeNode root = new TreeNode(0, e, f);
        int target = 0;
        TreeNode node = deleteNode(root, target)[0];
    }

    // [root, right most leaf]
    private TreeNode[] deleteNode(TreeNode root, int target) {
        if (root == null) return new TreeNode[]{null, null};

        TreeNode[] left = deleteNode(root.left, target);
        TreeNode[] right = deleteNode(root.right, target);
        if (root.val != target) {
            root.left = left[0];
            root.right = right[0];
            return new TreeNode[]{root, right[1] == null ? root : right[1]};
        }

        if (left[0] == null && right[0] == null) return left;
        if (left[0] != null && right[0] != null) {
            left[1].right = right[0];
            return new TreeNode[]{left[0], right[1]};
        }
        return left[0] == null ? right : left;
    }
}
