package multithread;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InterruptWithFinallyTest {

    @Test
    public void test() {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("finally executed");
            }
        });
        executor.shutdown(); // This does not cancel the already-scheduled task.

        executor.shutdownNow();
    }
}
