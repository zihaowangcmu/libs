package leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LC889 {

    @Test
    public void test() {
        int[] a = new int[]{1,2,4,5,3,6,7};
        int[] b = new int[]{4,5,2,6,7,3,1};
        TreeNode treeNode = constructFromPrePost(a, b);
    }

//    Map<Integer, Integer> preMap = new HashMap();
//    Map<Integer, Integer> postMap = new HashMap();
//    public TreeNode constructFromPrePost(int[] pre, int[] post) {
//        for (int i = 0; i < pre.length; i++) preMap.put(pre[i], i);
//        for (int i = 0; i < pre.length; i++) preMap.put(post[i], i);
//        return build(pre, 0, pre.length - 1, post, 0, post.length - 1);
//    }
//
//    private TreeNode build(int[] pre, int preS, int preE, int[] post, int postS, int postE) {
//        if (preS > preE) return null;
//        if (preS == preE) return new TreeNode(pre[preS]);
//
//        TreeNode root = new TreeNode(pre[preS]);
//        Map<Integer, Integer> left = new HashMap();
//        int d = 1;
//        for (; d <= preE - preS; d++) {
//            left.putIfAbsent(pre[preS + d], 0);
//            left.put(pre[preS + d], left.get(pre[preS + d]) + 1);
//            if (left.get(pre[preS + d]) == 0) left.remove(pre[preS + d]);
//            left.putIfAbsent(post[postS + d - 1], 0);
//            left.put(post[postS + d - 1], left.get(post[postS + d - 1]) - 1);
//            if (left.get(post[postS + d - 1]) == 0) left.remove(post[postS + d - 1]);
//            if (left.size() == 0) break;
//        }
//        root.left = build(pre, preS + 1, preS + d, post, postS, postS + d - 1);
//        root.right = build(pre, preS + d + 1, preE, post, postS + d, postE - 1);
//        return root;
//    }


    int preIndex = 0, posIndex = 0;
    public TreeNode constructFromPrePost(int[]pre, int[]post) {
        TreeNode root = new TreeNode(pre[preIndex++]);
        if (root.val != post[posIndex])
            root.left = constructFromPrePost(pre, post);
        if (root.val != post[posIndex])
            root.right = constructFromPrePost(pre, post);
        posIndex++;
        return root;
    }
}
