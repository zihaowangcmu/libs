package utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadUtils {

    /**
     * Init threads with specific name and count.
     * @param nameFormat
     * @param threadNum
     * @return
     */
    public static ExecutorService initThread(String nameFormat, int threadNum) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(nameFormat).build();
        return Executors.newFixedThreadPool(threadNum, threadFactory);
    }

    /**
     * Init single thread with specific name format.
     * @param nameFormat
     * @return
     */
    public static ExecutorService initSingleThread(String nameFormat) {
        return initThread(nameFormat, 1);
    }
}
