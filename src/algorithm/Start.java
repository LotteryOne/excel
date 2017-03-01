package algorithm;

import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created by BlueSky on 2/25/2017.
 */
public class Start {

    /**
     * 单项链表 存取
     */

    @Test
    public void TaskSingle() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        node1.next = node2;
        ListNode node3 = new ListNode(3);
        node2.next = node3;

        printStack(node1);

    }

    public void printStack(ListNode node) {
        Stack<ListNode> stack = new Stack<>();
        while (node != null) {
            stack.push(node);
            node = (ListNode) node.next;
        }

        while (!stack.empty()) {
            System.out.println(stack.pop().data);
        }

    }

    /**
     * 重建二叉树
     */
    @Test
    public void buildBinaryTree() throws Exception {
        int[] preSort = {1, 2, 4, 7, 3, 5, 6, 8};
        int[] inSort = {4, 7, 2, 1, 5, 3, 8, 6};
        BinaryTreeNode root = construcCore(preSort, inSort);
//        System.out.println(root.value);
    }

    private BinaryTreeNode construcCore(int[] preSort, int[] inSort) throws Exception {

        if (preSort == null || inSort == null) {
            return null;
        }
        if (preSort.length != inSort.length) {
            throw new Exception("长度不一致，无法重建");
        }

        BinaryTreeNode root = new BinaryTreeNode();

        for (int i = 0; i < inSort.length; i++) {
            if (inSort[i] == preSort[0]) {

                root.value = inSort[i];
                System.out.println(root.value);
                root.leftNode = construcCore(Arrays.copyOfRange(preSort, 1, i + 1),
                        Arrays.copyOfRange(inSort, 0, i));

                root.rightNode = construcCore(Arrays.copyOfRange(preSort, i + 1, preSort.length),
                        Arrays.copyOfRange(inSort, i + 1, inSort.length));

            }
        }


        return root;

    }


}
