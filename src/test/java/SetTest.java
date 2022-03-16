import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;

public class SetTest {

    @Test
    public void containsNullTest() {
        Set<Integer> set = new HashSet();
        assertFalse(set.contains(null));
    }
}
