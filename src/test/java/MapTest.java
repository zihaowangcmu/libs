import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertNotEquals;

public class MapTest {

    @Test
    public void modifyIntValTest() {
        Map<String, Integer> map = new HashMap();
        map.put("one", 1);
        Integer one = map.get("one");
        one++;
        System.out.println(map.get("one"));
        System.out.println(one);
        assertNotEquals(one, map.get("one"));
    }

    @Test
    public void forEachTest() {
        Map<String, Integer> map = new HashMap();
        map.put("one", 1);
        map.put("two", 2);
        List<String> list = new ArrayList<>();

        map.forEach((key, value) -> {
            list.add(key + " " + value);
        });

        list.forEach(System.out::println);
    }

    @Test
    public void keySetTest() {
        Map<Integer, Integer> map = new ConcurrentHashMap<>();
        map.put(1, 1);
        map.keySet().remove(1);
        System.out.println(map.size());
    }
}

