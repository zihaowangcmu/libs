package log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogError {

    private static final Logger logger = LoggerFactory.getLogger(LogError.class);

    @Test
    public void test() {
        Exception e = new NullPointerException("Test NPE in LogError.class.");
        logger.error("Caught error: {}", e.toString());
    }
}
