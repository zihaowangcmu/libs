package interview.google.onsite;

import io.vertx.core.json.Json;
import leetcode.TrieNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 分隔一个字符串，要么分割成单个字符，要么如果这部分可以用之前已经分割的部分再加上一个新的字符构造。
 * 所有子字符串必须最长。例子: gooogle -> 'g' , 'o', 'oo', 'gl', 'e'.
 */
public class SplitString {

    @Test
    public void test() {
        String s = "goooogle";
        List<String> strings = split(s);
        System.out.println(Json.encode(strings));
    }

    List<String> res = new ArrayList<>();
    TrieNode root = new TrieNode();
    private List<String> split(String s) {
        int i = 0;
        while (i < s.length()) {
            int ni = split(s, i, root);
            res.add(s.substring(i, ni));
            i = ni;
        }
        return res;
    }

    private int split(String s, int p, TrieNode root) {
        if (p == s.length()) return p;
        int pos = s.charAt(p) - 'a';
        if (root.nodes[pos] == null) {
            root.nodes[pos] = new TrieNode();
            return p + 1;
        }
        return split(s, p + 1, root.nodes[pos]);
    }
}
