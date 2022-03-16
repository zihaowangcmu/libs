package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class LC332 {

    @Test
    public void test() {
        List<List<String>> tickets = new ArrayList<>();
//        tickets.add(Arrays.asList("MUC","LHR"));
//        tickets.add(Arrays.asList("JFK","MUC"));
//        tickets.add(Arrays.asList("SFO","SJC"));
//        tickets.add(Arrays.asList("LHR","SFO"));

        // [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
        tickets.add(Arrays.asList("JFK","ATL"));
        tickets.add(Arrays.asList("JFK","SFO"));
        tickets.add(Arrays.asList("SFO","ATL"));
        tickets.add(Arrays.asList("ATL","JFK"));
        tickets.add(Arrays.asList("ATL","SFO"));
        System.out.println(findItinerary(tickets));

//        String[] a = new String[]{"abc", "a"};
//        Arrays.algorithm.sort(a);
//        System.out.println(a[0]);
    }

    public List<String> findItinerary(List<List<String>> tickets) {
        Map<String, Set<String>> map = new HashMap();
        for (List<String> l : tickets) {
            String from = l.get(0), to = l.get(1);
            map.putIfAbsent(from, new HashSet());
            map.get(from).add(to);
        }
        int steps = tickets.size(), curStep = 0;
        Queue<Path> q = new LinkedList();
        q.offer(new Path());
        while (!q.isEmpty()) {
            if (curStep == steps) break;
            int size = q.size();
            while (size-- > 0) {
                Path cur = q.poll();
                String last = cur.p.get(curStep);
                for (String to : map.get(last)) {
                    if (cur.m.get(last).contains(to)) continue;
                    Path child = new Path(cur);
                    child.p.add(to);
                    child.m.putIfAbsent(to, new HashSet());
                    child.m.get(last).add(to);
                    q.offer(child);
                }
            }
            curStep++;
        }
        Path min = q.poll();
        while (!q.isEmpty()) {
            Path cur = q.poll();
            int ptr = 0;
            while (++ptr <= steps) {
                if (cur.p.get(ptr).compareTo(min.p.get(ptr)) > 0) break;
                if (cur.p.get(ptr).compareTo(min.p.get(ptr)) < 0) {
                    min = cur;
                    break;
                }
            }
        }
        return min.p;
    }

    class Path {
        List<String> p;
        Map<String, Set<String>> m;
        public Path() {
            p = new ArrayList();
            m = new HashMap();
            p.add("JFK");
            m.put("JFK", new HashSet());
        }

        public Path(Path other) {
            p = new ArrayList(other.p);
            // shallow copy of set???
//            m = new HashMap(other.m);

            m = new HashMap<>();
            for (String k : other.m.keySet()) {
                m.put(k, new HashSet<>(other.m.get(k)));
            }
        }
    }
}
