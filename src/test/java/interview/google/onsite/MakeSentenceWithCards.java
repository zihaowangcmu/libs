package interview.google.onsite;

import io.vertx.core.json.Json;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 给你n张纸片，正反面写有字母。问怎样排列这些纸片，能够排出来给定的句子。所有纸片都要用到。
 * 我用dfs解决了。优化可以使用trie。面试官听到trie的优化方案很兴奋。
 */
public class MakeSentenceWithCards {

    Map<String, Boolean> memo = new HashMap<>();

    @Test
    public void test() {
        List<char[]> cards = Arrays.asList(new char[]{'a', 'b'}, new char[]{'m', 'n'}, new char[]{'n', 'b'}, new char[]{'m', 'c'});
        String target = "nmab";
        List<Integer> list = solve(cards, target);
        System.out.println(Json.encode(list));
    }

    private List<Integer> solve(List<char[]> cards, String target) {
        if (target.length() != cards.size()) return null;
        List<Integer> res = new ArrayList<>();
        if (!dfs(cards, target, 0, res, new TreeSet<>())) return null;
        return res;
    }

    private boolean dfs(List<char[]> cards, String target, int cur, List<Integer> res, Set<Integer> used) {
        if (cur == target.length()) return true;
        String key = key(cur, used);
        if (memo.containsKey(key)) return memo.get(key);

        char c = target.charAt(cur);
        for (int i = 0; i < cards.size(); i++) {
            if (!used.contains(i) && (cards.get(i)[0] == c || cards.get(i)[1] == c)) {
                used.add(i);
                res.add(i);
                if (dfs(cards, target, cur + 1, res, used)) {
                    memo.put(key, true);
                    return true;
                }
                used.remove(i);
                res.remove(res.size() - 1);
            }
        }
        memo.put(key, false);
        return false;
    }

    private String key(int cur, Set<Integer> used) {
        StringBuilder sb = new StringBuilder();
        sb.append(cur + " ");
        for (Integer k : used) {
            sb.append(k + " ");
        }
        return sb.toString();
    }

}
