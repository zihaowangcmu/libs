import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * To avoid confusion, use:
 * addFirst()/addLast()
 * getFirst()/getLast()
 * pollFirst()/pollLast()
 */
public class DequeTest {

    private Logger logger = LoggerFactory.getLogger(DequeTest.class);

    @Test
    public void addFirstTest() {
        Deque<Integer> deque = new LinkedBlockingDeque<>();

        deque.addLast(1);
        logger.info("Current head is {}, tail is {}", deque.getFirst(), deque.getLast());
        deque.addLast(2);
        logger.info("Current head is {}, tail is {}", deque.getFirst(), deque.getLast());
        deque.addFirst(0);
        logger.info("Current head is {}, tail is {}", deque.getFirst(), deque.getLast());
    }
}
