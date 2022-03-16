package interview.amazon.onsite.linuxfind;

import java.util.ArrayList;
import java.util.List;

public class MyFile {
    boolean isFile;
    String content;
    int type;
    List<MyFile> children;

    public MyFile(boolean isFile, int type) {
        this.isFile = isFile;
        this.type = type;
        this.children = new ArrayList<>();
    }

    public MyFile(String content, int type) {
        this(true, type);
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public int getSize() {
        return this.content == null ? 0 : this.content.getBytes().length;
    }
}
