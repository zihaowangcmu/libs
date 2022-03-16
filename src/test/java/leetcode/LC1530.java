package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class LC1530 {

    @Test
    public void test() {
//        TreeNode
//        countPairs()
    }

    public int countPairs(TreeNode root, int distance) {
        // Set<String> set = new HashSet();
        int res = 0;
        Map<TreeNode, List<TreeNode>> map = new HashMap();
        Set<TreeNode> leaves = new HashSet();
        buildMap(root, null, map, leaves);
        for (TreeNode leaf : leaves) {
            Queue<TreeNode> q = new LinkedList();
            q.offer(leaf);
            Set<TreeNode> seen = new HashSet();
            // List<TreeNode> l = new ArrayList();
            // int num = -1;
            while (!q.isEmpty() && distance-- > 0) {
                int size = q.size();
                while (size-- > 0) {
                    TreeNode cur = q.poll();
                    if (seen.contains(cur)) continue;
                    seen.add(cur);
                    for (TreeNode t : map.get(cur)) {
                        q.offer(t);
                    }
                    // if (distance == 0) {
                    //     if (leaves.contains(cur)) res++;
                    // }
                }
            }
            res += q.size();
        }
        return res / 2;
    }

    private void buildMap(TreeNode node, TreeNode parent, Map<TreeNode, List<TreeNode>> map, Set<TreeNode> leaves) {
        if (node == null) return;
        if (!map.containsKey(node)) {
            map.put(node, new ArrayList());
            if (parent != null) {
                map.get(node).add(parent);
                map.get(parent).add(node);
            }
            if (node.left == null && node.right == null) {
                leaves.add(node);
            }
            buildMap(node.left, node, map, leaves);
            buildMap(node.right, node, map, leaves);
        }
    }
}
