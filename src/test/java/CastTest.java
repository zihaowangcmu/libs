import com.google.common.cache.Cache;
import model.Dog;
import model.Husky;
import model.Mode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CastTest {

    private final Logger logger = LoggerFactory.getLogger(CastTest.class);

    @Test
    public void castTest() {
        Dog dog = new Husky("MOCHA", 8);
        Husky husky = (Husky) dog;
        husky.destroyHouse();
        assertEquals(dog.getMode(), Mode.HAPPY);
    }

    /**
     * Use Long.valueOf((Integer) i)
     */
    @Test
    public void castIntToLong() {
        Object i = 1;
        Integer integer = (Integer) i;
        Long li = Long.valueOf(integer);
        assertTrue(li instanceof Long);
    }

    @Test
    public void castIntegerArrayToLongArray() {
        Object[] objs = new Integer[]{1, 2, 3};
        Integer[] ints = (Integer[]) objs;
        Arrays.stream(ints).forEach(i -> assertTrue(i instanceof Integer));

        Long[] longs = new Long[ints.length];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = Long.valueOf(ints[i]);
        }
        Arrays.stream(longs).forEach(l -> assertTrue(l instanceof Long));
    }
}
