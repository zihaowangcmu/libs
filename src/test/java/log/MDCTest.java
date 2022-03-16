package log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.ExecutorService;

import static utils.ThreadUtils.initThread;

/**
 * This class test how MDC works with different threads
 * Please check the logback.xml!
 */
public class MDCTest {

    private Logger logger = LoggerFactory.getLogger(MDCTest.class);

    @Test
    public void MDCTest() {
        MDC.put("LP", "zwang-test-LP");
        MDC.put("dbName", "testDB");
        logger.info("Test logger 1");
        MDC.clear();
        logger.info("Test logger 2");
    }

    /**
     * Not precise. Need to mock time difference
     * @throws InterruptedException
     */
    @Test
    public void differentThreadTest() throws InterruptedException {
        initThreadAndRun("thread-1");
        initThreadAndRun("thread-2");
        initThreadAndRun("thread-3");
        Thread.sleep(100);
    }

    private void initThreadAndRun(String threadName) {
        ExecutorService thread = initThread("threadName", 1);
        thread.execute(() -> {
            MDC.put("LP", threadName);
            logger.info("running....");
        });
        thread.shutdown();
    }

    @Test
    public void differentThreadRunningTimeTest() throws InterruptedException {
        /**
         * Thread 1
         * Set LP, then sleep 300ms
         */
        String threadName1 = "thread1";
        ExecutorService thread1 = initThread(threadName1, 1);
        thread1.execute(() -> {
            MDC.put("LP", threadName1);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("running....");
        });
        thread1.shutdown();

        /**
         * Thread 2
         * Set LP, then sleep 100ms
         */
        String threadName2 = "thread2";
        ExecutorService thread2 = initThread(threadName2, 1);
        thread2.execute(() -> {
            MDC.put("LP", threadName2);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("running....");
        });
        thread2.shutdown();

        /**
         * Thread 3
         * Set LP, then sleep 200ms
         */
        String threadName3 = "thread3";
        ExecutorService thread3 = initThread(threadName3, 1);
        thread3.execute(() -> {
            MDC.put("LP", threadName3);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("running....");
        });
        thread3.shutdown();

        /**
         * Expect to see info with LP in this order:
         * thread2
         * thread3
         * thread1
         */

        Thread.sleep(400);
    }
}
