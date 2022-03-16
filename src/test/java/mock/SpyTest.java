package mock;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

public class SpyTest {

    @Test
    public void test() throws Exception {
        Queue queue = new LinkedBlockingQueue();
        queue = spy(queue);
        queue.add(0);
        queue.add(1);
        System.out.println("Original size: " + queue.size());

//        when(queue.size()).thenReturn(-1);
//        System.out.println("Original size: " + queue.size());

//        when(queue.remove()).thenReturn(-1);
        when(queue, "remove").thenAnswer(invocationOnMock -> {
            System.out.println("mock");
            return -1;
        });
        int res1 = (int) queue.remove();
        System.out.println(res1);
        int res2 = (int) queue.remove();
        System.out.println(res2);

        System.out.println("size: " + queue.size());
    }

    @Test
    public void test2() {
        Queue queue = new LinkedBlockingQueue();
        queue.add(1);
        queue.add(2);
        System.out.println("Original size: " + queue.size());
        System.out.println(queue.toString());

        Queue mock = PowerMockito.mock(LinkedBlockingQueue.class);
        mock.add(3);
        mock.add(4);
        System.out.println(mock.size());
    }
}
