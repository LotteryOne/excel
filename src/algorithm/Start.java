package algorithm;

import org.junit.Test;

import java.util.Stack;

/**
 * Created by BlueSky on 2/25/2017.
 */
public class Start {

    /**
     * 单项链表
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


}
