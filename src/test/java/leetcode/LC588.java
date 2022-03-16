package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LC588 {

    @Test
    public void test() {
        FileSystem fileSystem = new FileSystem();
        fileSystem.ls("/");
        fileSystem.mkdir("/a/b/c");
        fileSystem.addContentToFile("/a/b/c/d", "hello");
        fileSystem.ls("/");
        fileSystem.readContentFromFile("/a/b/c/d");
    }

    class FileSystem {
        FileFolder root;
        public FileSystem() {
            root = new FileFolder("/");
        }

        public List<String> ls(String path) {
            FileFolder cur = get(path);
            List<String> res = new ArrayList();
            if (!cur.isFolder) res.add(cur.name);
            else {
                for (String key : cur.map.keySet()) res.add(key);
            }
            return res;
        }

        public void mkdir(String path) {
            String[] p = path.split("/");
            // skip the first ""
            mkdir(p, 1, p.length);
        }

        private FileFolder mkdir(String[] p, int start, int end) {
            FileFolder cur = root;
            for (int i = start; i < end; i++) {
                if (!cur.map.containsKey(p[i])) {
                    cur.map.put(p[i], new FileFolder(p[i]));
                }
                cur = cur.map.get(p[i]);
            }
            return cur;
        }

        public void addContentToFile(String filePath, String content) {
            String[] p = filePath.split("/");
            // skip the first ""
            FileFolder cur = mkdir(p, 1, p.length - 1);
            String fileName = p[p.length - 1];
            if (!cur.map.containsKey(fileName)) cur.map.put(fileName, new FileFolder(fileName, false));
            cur.map.get(fileName).content.append(content);
        }

        public String readContentFromFile(String filePath) {
            return get(filePath).content.toString();
        }

        private FileFolder get(String path) {
            String[] p = path.split("/");
            FileFolder cur = root;
            for (int i = 1  ; i < p.length; i++) {
                cur = cur.map.get(p[i]);
            }
            return cur;
        }

        class FileFolder {
            String name = "";
            boolean isFolder = true;
            StringBuilder content = new StringBuilder();
            Map<String, FileFolder> map = new HashMap();
            public FileFolder(String name) {
                this(name, true);
            };
            public FileFolder(String name, boolean isFolder) {
                this.name = name;
                this.isFolder = isFolder;
            };
        }
    }


}
