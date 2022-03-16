package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LC691 {

    @Test
    public void test() {
        String target = "putcat";
        String[] stickers = new String[]{"city","would","feel","effect","cell","paint"};
        System.out.println(minStickers(stickers, target));
    }

    Map<Character, Integer> order = new HashMap();
    List<int[]> labels = new ArrayList();
    Map<String, Integer>[] memo;
    public int minStickers(String[] stickers, String target) {

        int index = 0;
        for (char c : target.toCharArray()) {
            if (!order.containsKey(c)) {
                order.put(c, index++);
            }
        }
        // build tar
        int[] tar = new int[index];
        for (char c : target.toCharArray()) {
            tar[order.get(c)]++;
        }

        this.memo = new HashMap[index + 1];
        for (int i = 0; i < index + 1; i++) memo[i] = new HashMap();

        Set<Character> unseen = new HashSet(order.keySet());
        // freq of chars in target of stickers
        List<int[]> list = new ArrayList();
        for (String s : stickers) {
            int[] cur = new int[index];
            boolean atLeastOne = false;
            for (char c : s.toCharArray()) {
                if (order.containsKey(c)) {
                    unseen.remove(c);
                    atLeastOne = true;
                    cur[order.get(c)]++;
                }
            }
            if (atLeastOne) labels.add(cur);
        }
        if (!unseen.isEmpty()) return -1;


        // remove bad candidates
        // from end to start, so remove of labels won't affect
        // must remove, or equal case is not handled
        for (int i = labels.size() - 1; i >= 0; i--) {
            int[] l1 = labels.get(i);
            boolean put = true;
            for (int j = 0; j < labels.size(); j++) {
                if (i == j) continue;
                int[] l2 = labels.get(j);
                boolean cand = false;
                for (int k = 0; k < index; k++) {
                    if (l1[k] > l2[k]) {
                        cand = true;
                        break;
                    }
                }
                if (!cand) {
                    put = false;
                    break;
                }
            }
            if (put) {
                list.add(l1);
            } else {
                labels.remove(i);
            }
        }
        labels = list;


        return dfs(0, tar, 0);
    }

    private int dfs(int cur, int[] tar, int zeros) {
        if (zeros == tar.length) return 0;
        if (cur == labels.size()) return -1;

        String key = key(tar);
        if (memo[cur].containsKey(key)) return memo[cur].get(key);
        int res = Integer.MAX_VALUE;
        // no pick cur
        int next = dfs(cur + 1, Arrays.copyOf(tar, tar.length), zeros);
        if (next != -1) res = next;

        // pick it, but since we can still pick more than once, we don't change the cur in dfs
        boolean need = false;
        for (int i = 0; i < tar.length; i++) {
            if (labels.get(cur)[i] > 0 && tar[i] > 0) {
                need = true;
                break;
            }
        }
        if (need) {
            int[] ntar = Arrays.copyOf(tar, tar.length);
            int nzeros = 0;
            for (int i = 0; i < ntar.length; i++) {
                if (labels.get(cur)[i] > 0 && ntar[i] > 0) {
                    ntar[i] = Math.max(ntar[i] - labels.get(cur)[i], 0);
                }
                if (ntar[i] == 0) nzeros++;
            }
            next = dfs(cur, ntar, nzeros);
            if (next != -1) res = Math.min(res, next + 1);
        }
        if (res == Integer.MAX_VALUE) res = -1;
        memo[cur].put(key, res);
        return res;
    }

    private String key(int[] tar) {
        StringBuilder sb = new StringBuilder();
        for (int i : tar) sb.append(i + "");
        return sb.toString();
    }
}
