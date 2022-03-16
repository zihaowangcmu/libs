package interview.phone2;

import leetcode.ListNode;
import org.junit.Test;

/**
 * Given a singly linked list, remove nodes greater than X.
 * List = 100 → 105 → 50, X = 100
 * List becomes 100 → 50
 * Return a reference to the root node of the list after removing 105.
 *
 * removeNodes has the following parameter(s):
 * listHead:a reference to the root node of the singly-linked list
 * x:integer, the maximum value to be included in the returned singly-linked list
 */
public class RemoveGreaterListNode {

    @Test
    public void test() {
        ListNode node3 = new ListNode(50);
        ListNode node2 = new ListNode(105, node3);
        ListNode node1 = new ListNode(100, node2);
        int n = 10;
        ListNode node = remove(node1, n);
    }

    public ListNode remove(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode pre = dummy;
        ListNode cur = head;
        while (cur != null) {
            if (cur.val > n) {
                pre.next = cur.next;
            } else {
                pre = cur;
            }
            cur = cur.next;
        }
        return dummy.next;
    }
}
