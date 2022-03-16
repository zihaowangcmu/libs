package leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LC146 {
    @Test
    public void test() {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 0);
        cache.put(2, 2);
        int v1 = cache.get(1);
        cache.put(3, 3);
        int v2 = cache.get(2);
        cache.put(4, 4);
        int v3 = cache.get(1);
    }

    class LRUCache {
        Map<Integer, Node> map;
        Node head;
        Node tail;
        int cap;
        public LRUCache(int capacity) {
            this.map = new HashMap<>();
            this.head = new Node(-1, -1);
            this.tail = new Node(-1, -1);
            head.next = tail;
            tail.prev = head;
            this.cap = capacity;
        }

        public int get(int key) {
            if (!map.containsKey(key)) return -1;
            moveToTail(key);
            return map.get(key).val;
        }

        private void moveToTail(int k) {
            Node cur = map.get(k);
            cur.prev.next = cur.next;
            cur.next.prev = cur.prev;
            cur.next = tail;
            cur.prev = tail.prev;
            tail.prev.next = cur;
            tail.prev = cur;
        }

        private void evictHead() {
            int k = head.next.key;
            map.remove(k);
            head.next = head.next.next;
            head.next.prev = head;
        }

        private void add(int key, int value) {
            Node cur = new Node(key, value);
            map.put(key, cur);
            cur.next = tail;
            cur.prev = tail.prev;
            tail.prev = cur;
            cur.prev.next = cur;
        }

        public void put(int key, int value) {
            if (map.containsKey(key)) {
                map.get(key).val = value;
                moveToTail(key);
            } else {
                if (map.size() == cap) evictHead();
                add(key, value);
            }
        }

        class Node {
            int val;
            int key;
            Node prev;
            Node next;

            public Node(int k, int v) {
                this.key = k;
                this.val = v;
            }
        }
    }
}
