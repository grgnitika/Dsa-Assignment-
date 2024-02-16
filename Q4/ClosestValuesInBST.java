// You are provided with balanced binary tree with the target value k. return x number of values 
// that are closest to the given target k. provide solution in O(n)
// Note: You have only one set of unique values x in binary search tree that are closest to the 
// target.
// Input: 
// K=3.8
// x=2
// Output: 3,4

package Q4;

import java.util.*;

class TreeNode {
    int val;
    TreeNode left, right;
    
    public TreeNode(int val) {
        this.val = val;
        this.left = this.right = null;
    }
}

public class ClosestValuesInBST {
    public static List<Integer> closestValues(TreeNode root, double target, int x) {
        List<Integer> result = new ArrayList<>();
        List<Integer> inorder = new ArrayList<>();
        inorderTraversal(root, inorder);
        
        int left = 0, right = inorder.size() - 1;
        while (right - left + 1 > x) {
            double diffLeft = Math.abs(inorder.get(left) - target);
            double diffRight = Math.abs(inorder.get(right) - target);
            if (diffLeft > diffRight) {
                left++;
            } else {
                right--;
            }
        }
        
        for (int i = left; i <= right; i++) {
            result.add(inorder.get(i));
        }
        
        return result;
    }
    
    private static void inorderTraversal(TreeNode root, List<Integer> inorder) {
        if (root == null) return;
        
        inorderTraversal(root.left, inorder);
        inorder.add(root.val);
        inorderTraversal(root.right, inorder);
    }
    
    public static void main(String[] args) {
        // Example BST:          5
        //                     /    \
        //                    3      6
        //                   / \    / \
        //                  2   4  -   7
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(3);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.right.right = new TreeNode(7);
        
        double target = 3.8;
        int x = 2;
        List<Integer> closestVals = closestValues(root, target, x);
        System.out.println("Closest values to the given target " + target + " with " + x + " values: " + closestVals);
    }
}