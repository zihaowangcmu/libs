import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeTest {

    private final Logger logger = LoggerFactory.getLogger(TimeTest.class);

    /**
     * Check if there is much difference between DateTime.now().getMillis() and System.currentTimeMillis()
     */
    @Test
    public void DateTimeVSSystemTime() {
        long dateTimeMill = DateTime.now().getMillis();
        long systemTimeMill = System.currentTimeMillis();
        logger.info("dateTimeMill: {}", dateTimeMill);
        logger.info("systemTimeMill: {}", systemTimeMill);
    }
}
