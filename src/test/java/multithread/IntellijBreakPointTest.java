package multithread;

import org.junit.Test;
import utils.ThreadUtils;

import java.util.concurrent.ExecutorService;

public class IntellijBreakPointTest {

    @Test
    public void test() throws InterruptedException {
        ExecutorService executorService = ThreadUtils.initThread("intellij-test-thread-", 2);
        runThread1(executorService);
        runThread2(executorService);
        Thread.sleep(1000);
        System.out.println("end of main thread");
    }

    private void runThread1(ExecutorService executorService) {
        executorService.execute(() -> {
            System.out.println("Start 1");
            while (true) {
                System.out.println("running 1...");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void runThread2(ExecutorService executorService) {
        executorService.execute(() -> {
            System.out.println("Start 2");
            while (true) {
                System.out.println("running 2...");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
