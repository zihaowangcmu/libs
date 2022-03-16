package log;

import model.Dog;
import model.Husky;
import io.vertx.core.json.Json;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class LogPerformance {

    Logger logger = LoggerFactory.getLogger(LogPerformance.class);

    // Need to change debug level
    @Test
    public void testPerformanceOnDebug() {
        Dog dog1 = new Husky("TT", 1);

        long t1 = System.currentTimeMillis();
        logger.debug("Bad string: " + Json.encode(dog1));
        long t2 = System.currentTimeMillis();
        long ti12 = t2 - t1;

        Dog dog2 = new Husky("WW", 1);

        long t3 = System.currentTimeMillis();
        logger.debug("Bad string: {}", Json.encode(dog2));
        long t4 = System.currentTimeMillis();
        long ti34 = t4 - t3;

        System.out.println("Bad string format takes: " + ti12);
        System.out.println("Good string format takes: " + ti34);
        assertTrue(ti12 > ti34);
    }

    @Test
    public void testStringFormat() {
        logger.info(String.format("Test %s", "testStringFormat"));
        logger.info("Test {}", "testStringFormat");

        String s1 = "test11111";
        String s2 = "test222";
        String s3 = "test3";
        logger.info("Test 1 {}, Test 2 {}, Test 3 {}", s1, s2, s3);
    }
}
