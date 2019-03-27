package sort;


import java.util.LinkedList;
import java.util.Queue;

public class Solution {

    public static void main(String[] args) {
        TreeNode tree = new TreeNode(5);
        tree.left = new TreeNode(4);
        tree.right = new TreeNode(8);
        tree.left.left = new TreeNode(11);
        tree.right.left = new TreeNode(13);
        tree.right.right = new TreeNode(4);
        tree.left.left.left = new TreeNode(7);
        tree.left.left.right = new TreeNode(2);
        tree.right.right.right = new TreeNode(1);
        System.out.println(new Solution().print(tree));
    }

    public boolean print(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode last = root;
        TreeNode nLast = root;
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode head = queue.poll();
            // 处理
            System.out.print(head.val + ", ");
            if (head.left != null) {
                queue.offer(head.left);
                nLast = head.left;
            }
            if (head.right != null) {
                queue.offer(head.right);
                nLast = head.right;
            }
            if (head == last) {
                last = nLast;
                System.out.println();
            }
        }
        return false;
    }

}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}

