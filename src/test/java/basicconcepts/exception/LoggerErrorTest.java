package basicconcepts.exception;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerErrorTest {

    Logger logger = LoggerFactory.getLogger(LoggerErrorTest.class);

    @Test
    public void test() {
        try {
            throwExceptionHelper();
        } catch (Exception e) {
            logger.error("Caught error: ", e);
        }
    }

    private void throwExceptionHelper() throws Exception {
        throw new NullPointerException("Test NPE");
    }

    @Test
    public void test2() {
        try {
            throwExceptionHelper();
        } catch (Exception e) {
            logger.error("Caught error: {}", Thread.currentThread().getName(), e);
        }
    }
}
