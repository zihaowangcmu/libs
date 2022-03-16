import io.vertx.core.json.Json;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ThreadUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    private static Logger logger = LoggerFactory.getLogger(StringTest.class);

    @Test
    public void streamTest() {
        List<Integer> list = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());
        List<Integer> dest = new ArrayList<>();
        list.stream().map(dest::add).collect(Collectors.toSet());
//        list.stream().peek(dest::add).collect(Collectors.toSet());
        logger.info("Dest: {}", Json.encode(dest));
    }

    @Test
    public void parallelStreamListTest() {
        List<Integer> list = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());
        logger.info("Sequential stream");
        list.stream().forEach(x -> logger.info(String.valueOf(x)));

        logger.info("Parallel stream");
        list.parallelStream().forEach(x -> logger.info(String.valueOf(x)));
    }

    /**
     * Use entrySet if you want to use stream on map
     */
    @Test
    public void parallelStreamMapTest() {
        Map<Integer, Integer> map = Stream.of(1, 2, 3, 4, 5)
                .collect(Collectors.toMap((k) -> (int) k, (k) -> (int) k));
        logger.info("Sequential stream");
        map.entrySet().stream().forEach(x -> logger.info(String.valueOf(x.getValue())));

        logger.info("Parallel stream");
        map.entrySet().parallelStream().forEach(x -> logger.info(String.valueOf(x.getValue())));
    }

    /**
     * Why only 1 worker is used?
     * @throws InterruptedException
     */
    @Test
    public void parallelStreamWithExecutorService() throws InterruptedException {
        List<Integer> list = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());
        ExecutorService worker = ThreadUtils.initThread("workers-%d", 10);
        logger.info("Sequential stream");
        list.stream().forEach(x -> logger.info(String.valueOf(x)));

        logger.info("Parallel stream");
        worker.execute(() -> {
            list.parallelStream().forEach(x -> logger.info(String.valueOf(x)));
        });
        Thread.sleep(20);
    }

    @Test
    public void parallelStreamWithForkJoinPool() throws InterruptedException {
        List<Integer> list = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());
        ForkJoinPool customThreadPool = new ForkJoinPool(5);
        logger.info("Parallel stream");
        customThreadPool.submit(() -> {
            list.parallelStream().forEach(x -> logger.info(String.valueOf(x)));
        });
        Thread.sleep(20);
    }
}
