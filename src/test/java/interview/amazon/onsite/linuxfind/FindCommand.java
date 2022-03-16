package interview.amazon.onsite.linuxfind;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.com/discuss/interview-question/369272/Amazon-or-Onsite-or-Linux-Find-Command
 */
public class FindCommand {

    public List<MyFile> find(MyFile root, Filter filter) {
        List<MyFile> res = new ArrayList<>();
        helper(root, filter, res);
        return res;
    }

    private void helper(MyFile root, Filter filter, List<MyFile> res) {
        if (root.isFile) {
            if (filter.validate(root)) {
                res.add(root);
            }
            return;
        }

        for (MyFile subFile : root.children) {
            helper(subFile, filter, res);
        }
        return;
    }
}
