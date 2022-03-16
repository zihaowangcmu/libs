import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class AtomicValueTest {

    @Test
    public void test1() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        atomicInteger.compareAndSet(atomicInteger.get(), 1);
        assertEquals(atomicInteger.get(), 1);
    }
}
