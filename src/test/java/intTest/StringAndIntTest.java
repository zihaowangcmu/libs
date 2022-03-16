package intTest;

import com.tracelink.dnp.utils.functional.Try;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StringAndIntTest {

    @Test
    public void parseIntTest() {
        String s;

        s = "10";
        assertEquals(10, Integer.parseInt(s));

        s = "0001";
        assertEquals(1, Integer.parseInt(s));

        s = "1a";
        try {
            Integer.parseInt(s);
            fail("Did not throw basic.exception.");
        } catch (Exception e) {
            // succeeds
        }

        Try<String> t = Try.ofNullable(null);
    }
}
