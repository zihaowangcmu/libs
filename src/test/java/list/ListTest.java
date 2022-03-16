package list;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListTest {

    @Test
    public void toStringTest() {
        List<String> list = new ArrayList<>();
        list.add("a");
        System.out.println(list);
        System.out.println(list.toString());

        list = List.of("a", "b");
        System.out.println(list);
    }
}
