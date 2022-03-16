package interview.google.phone;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *https://www.1point3acres.com/bbs/forum.php?mod=viewthread&tid=646548&extra=&highlight=google%2B%C3%E6&page=1
 * 给你一些"alphabet blocks"，每个block像筛子一个有6个面，每个面上有一个letter。函数inputs是一条message和一些blocks。
 * 函数应该返回那些blocks能够拼凑出那条message的每个字母。每个block只能用一次。
 * 例子：
 * blocks = { {abcdef}, {bcdefg}, {ppqqrr} }
 * message = "re"
 * result = { {ppqqrr}, {abcdef} } OR { {ppqqrr}, {bcdefg} }
 * 原题：蠡口/discuss/interview-question/267985/google-interview-construct-a-word-using-dice
 */
public class ConstructAWordWithDice {

    @Test
    public void test() {
        String message = "re";
        String[] dices = new String[]{"abcdef", "bcdefg", "ppqqrr"};
        List<int[]> res = solve(message, dices);
    }

    private List<int[]> solve(String message, String[] dices) {
        if (dices.length < message.length()) return new ArrayList<>();
        List<Integer>[] nums = new ArrayList[26];
        for (int i = 0; i < 26; i++) {
            nums[i] = new ArrayList<>();
        }
        for (int i = 0; i < dices.length; i++) {
            boolean[] visited = new boolean[26];
            for (char c : dices[i].toCharArray()) {
                if (!visited[c - 'a']) {
                    visited[c - 'a'] = true;
                    nums[c - 'a'].add(i);
                }
            }
        }
        List<int[]> res = new ArrayList<>();
        dfs(message, nums, res, 0, new int[message.length()], new HashSet<>());
        return res;
    }

    private void dfs(String s, List<Integer>[] nums, List<int[]> res, int cur, int[] prev, Set<Integer> used) {
        if (cur == s.length()) {
            res.add(prev);
            return;
        }

        int index = s.charAt(cur) - 'a';
        for (int candidate : nums[index]) {
            if (!used.contains(candidate)) {
                used.add(candidate);
                int[] next = Arrays.copyOf(prev, prev.length);
                next[cur] = candidate;
                dfs(s, nums, res, cur + 1, next, used);
                used.remove(candidate);
            }
        }
    }
}
