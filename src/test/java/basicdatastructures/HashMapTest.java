package basicdatastructures;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * https://segmentfault.com/a/1190000012926722
 */
public class HashMapTest {

    /**
     * key can be null
     * value can be null
     */
    @Test
    public void nullKeyValueTest() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(null, -1);
        System.out.println(map.get(null));

        System.out.println(map.get(0));
    }
}
