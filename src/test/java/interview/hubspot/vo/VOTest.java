package interview.hubspot.vo;

import io.vertx.core.json.Json;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class VOTest {
    // 2 lists of ints, sorted, not null, can be empty
    // l1: [1, 7, 15, 21]
    // l2: [2, 5]
    // n: 3 (>= 0, can > l1 + l2)
    // return the sorted merged result of the 2 lists up to size n
    // output: [1, 2, 5]

    @Test
    public void test() {
//        List<Integer> l1 = Arrays.asList(1, 7, 15, 21);
//        List<Integer> l2 = Arrays.asList();
//        int n = 0;
//        List<Integer> list = mergeTwoSortedList(l1, l2, n);
//        System.out.println(Json.encode(list));


//        List<Integer> l1 = Arrays.asList(1, 7, 15, 21);
//        List<Integer> l2 = Arrays.asList(10, 36);
//        List<Integer> l3 = Arrays.asList(2, 5);

        List<Integer> l1 = Arrays.asList();
        List<Integer> l2 = Arrays.asList();
        List<Integer> l3 = Arrays.asList();
        List<List<Integer>> lists = new ArrayList<>();
        lists.add(l1);
        lists.add(l2);
        lists.add(l3);
        int n = 1000;
        List<Integer> list = mergeSortedLists(lists, n);
        System.out.println(Json.encode(list));
    }

    public List<Integer> mergeSortedLists(List<List<Integer>> lists, int n) {
        List<Integer> res = new ArrayList<>();
        // [the id of the list, the pointer index of this list]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> (lists.get(a[0]).get(a[1]) - lists.get(b[0]).get(b[1])));
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).size() == 0) continue;
            pq.offer(new int[]{i, 0});
        }
        while (!pq.isEmpty() && res.size() < n) {
            int[] cur = pq.poll();
            res.add(lists.get(cur[0]).get(cur[1]));
            if (cur[1] + 1 < lists.get(cur[0]).size()) {
                cur[1]++;
                pq.offer(cur);
            }
        }
        return res;
    }

    public List<Integer> mergeTwoSortedList(List<Integer> l1, List<Integer> l2, int n) {
        List<Integer> res = new ArrayList<>();
        int i = 0, j = 0;
        while (i < l1.size() && j < l2.size() && res.size() < n) {
            int num1 = l1.get(i), num2 = l2.get(j);
            if (num1 <= num2) {
                res.add(num1);
                i++;
            } else {
                res.add(num2);
                j++;
            }
        }
        if (res.size() == n) return res;
        while (i < l1.size() && res.size() < n) {
            res.add(l1.get(i++));
        }
        while (j < l2.size() && res.size() < n) {
            res.add(l2.get(j++));
        }
        return res;
    }



}
