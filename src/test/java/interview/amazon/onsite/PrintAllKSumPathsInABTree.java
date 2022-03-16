package interview.amazon.onsite;

import com.sun.source.tree.Tree;
import io.vertx.core.json.Json;
import leetcode.TreeNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PrintAllKSumPathsInABTree {

    @Test
    public void test() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(3);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(1);
        root.left.right.left = new TreeNode(1);
        root.right = new TreeNode(-1);
        root.right.left = new TreeNode(4);
        root.right.left.left = new TreeNode(1);
        root.right.left.right = new TreeNode(2);
        root.right.right = new TreeNode(5);
        root.right.right.right = new TreeNode(2);

        int k = 5;
        List<List<Integer>> res = allKSumPaths(root, k);
        System.out.println(Json.encode(res));
    }

    private List<Integer> path = new ArrayList<>();
    private List<List<Integer>> res = new ArrayList<>();
    private List<List<Integer>> allKSumPaths(TreeNode root, int k) {
        preorder(root, k);
        return res;
    }

    private void preorder(TreeNode root, int k) {
        if (root == null) return;

        path.add(root.val);
        preorder(root.left, k);
        preorder(root.right, k);
        int sum = 0;
        for (int i = path.size() - 1; i >= 0; i--) {
            sum += path.get(i);
            if (k == sum) {
                List<Integer> l = new ArrayList<>();
                for (int j = i; j < path.size(); j++) l.add(path.get(j));
                res.add(l);
            }
        }
        path.remove(path.size() - 1);
    }

//    @Test
//    public void test() {
//        Node root = new Node(1);
//        root.left = new Node(3);
//        root.left.left = new Node(2);
//        root.left.right = new Node(1);
//        root.left.right.left = new Node(1);
//        root.right = new Node(-1);
//        root.right.left = new Node(4);
//        root.right.left.left = new Node(1);
//        root.right.left.right = new Node(2);
//        root.right.right = new Node(5);
//        root.right.right.right = new Node(2);
//
//        int k = 5;
//        printKPath(root, k);
//    }

//        //utility function to print contents of
////a vector from index i to it's end
//        static void printVector(Vector<Integer> v, int i)
//        {
//            for (int j = i; j < v.size(); j++)
//                System.out.print(v.get(j) + " ");
//            System.out.println();
//        }
//
//        // binary tree node
//        static class Node
//        {
//            int data;
//            Node left,right;
//            Node(int x)
//            {
//                data = x;
//                left = right = null;
//            }
//        };
//        static Vector<Integer> path = new Vector<Integer>();
//
//        // This function prints all paths that have sum k
//        static void printKPathUtil(Node root, int k)
//        {
//            // empty node
//            if (root == null)
//                return;
//
//            // add current node to the path
//            path.add(root.data);
//
//            // check if there's any k sum path
//            // in the left sub-tree.
//            printKPathUtil(root.left, k);
//
//            // check if there's any k sum path
//            // in the right sub-tree.
//            printKPathUtil(root.right, k);
//
//            // check if there's any k sum path that
//            // terminates at this node
//            // Traverse the entire path as
//            // there can be negative elements too
//            int f = 0;
//            for (int j = path.size() - 1; j >= 0; j--)
//            {
//                f += path.get(j);
//
//                // If path sum is k, print the path
//                if (f == k)
//                    printVector(path, j);
//            }
//
//            // Remove the current element from the path
//            path.remove(path.size() - 1);
//        }
//
//        // A wrapper over printKPathUtil()
//        static void printKPath(Node root, int k)
//        {
//            path = new Vector<Integer>();
//            printKPathUtil(root, k);
//        }
}
