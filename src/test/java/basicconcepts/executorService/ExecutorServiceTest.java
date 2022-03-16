package basicconcepts.executorService;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// https://stackoverflow.com/questions/3929342/choose-between-executorservices-submit-and-executorservices-execute

public class ExecutorServiceTest {

    /**
     * ExecutorService can submit Runnable or Callable
     */
    @Test
    public void submitTest() {
        System.out.println("creating service");
        ExecutorService service = Executors.newFixedThreadPool(10);
        //ExtendedExecutor service = new ExtendedExecutor();

        // submit a Runnable
        service.submit(new Runnable(){
            public void run(){
                int a=4, b = 0;
                System.out.println("a and b="+a+":"+b);
                System.out.println("a/b:"+(a/b));
                System.out.println("Thread Name in Runnable after divide by zero:"+Thread.currentThread().getName());
            }
        });

        // submit a Callable
        service.submit(new Callable<String>() {
            /**
             * Computes a result, or throws an exception if unable to do so.
             *
             * @return computed result
             * @throws Exception if unable to compute a result
             */
            @Override
            public String call() throws Exception {
                return null;
            }
        });

        service.shutdown();
    }
}
