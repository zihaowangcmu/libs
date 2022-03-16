import org.junit.Test;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ComparatorTest {

    @Test
    public void test() {
        PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>(){

            @Override
            public int compare(String o1, String o2) {
                return 0;
            }
        });

        PriorityQueue<String> pq2 = new PriorityQueue<>((s1, s2) -> {
            return 1;
        });
    }
}
