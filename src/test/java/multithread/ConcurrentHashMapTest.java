package multithread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ConcurrentHashMapTest {

    // fail to reproduce, but not thread safe
    @Test
    public void mapPutGetTest() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        String key1 = "Key1";
        map.put(key1, 2);

        ExecutorService thread1 = createThread("Thread1", 1);
        ExecutorService thread2 = createThread("Thread2", 1);

        thread1.execute(() -> {
            System.out.println(System.currentTimeMillis() + "map.get in thread1: " + map.get(key1));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put(key1, map.get(key1) - 1);
            System.out.println(System.currentTimeMillis() + "map.get in thread1: " + map.get(key1));
        });

        thread2.execute(() -> {
            System.out.println(System.currentTimeMillis() + "map.get in thread2: " + map.get(key1));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put(key1, map.get(key1) - 1);
            System.out.println(System.currentTimeMillis() + "map.get in thread2: " + map.get(key1));
        });

        Thread.sleep(2000);
    }

    private ExecutorService createThread(String name, int threadNum) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(name).build();
        return Executors.newFixedThreadPool(threadNum, threadFactory);
    }

    /**
     * Thread safe way.
     * But the remove part is somewhat confusing.
     */
    @Test
    public void computeIfPresentTest() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        String key1 = "Key1";
        map.put(key1, 2);

        changeValue(map, key1, true);
        changeValue(map, key1, true);
    }

    private void changeValue(Map<String, Integer> map, String key, boolean isSuccess) {
        map.computeIfPresent(key, (k, v) -> {
            if (v == 2) {
                if (isSuccess) {
                    v = 1;
                } else {
                    v = -1;
                }
            } else {
                // It is fine to do it
                // Pay attention here. Different to changeValue2
                map.remove(key);
            }
            return v;
        });
        System.out.println(map.get(key));
        System.out.println(map.containsKey(key));
    }

    /**
     * Thread safe way.
     * Maybe less confusing.
     */
    @Test
    public void computeIfPresentTest2() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        String key1 = "Key1";
        map.put(key1, 2);

        changeValue2(map, key1, true);
        changeValue2(map, key1, true);
    }

    private void changeValue2(Map<String, Integer> map, String key, boolean isSuccess) {
        map.computeIfPresent(key, (k, v) -> {
            if (v == 2) {
                if (isSuccess) {
                    v = 1;
                } else {
                    v = -1;
                }
            } else {
                // It is fine to do it
                v = null;
            }
            return v;
        });
        System.out.println(map.get(key));
        System.out.println(map.containsKey(key));
    }
}
