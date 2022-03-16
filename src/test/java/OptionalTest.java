import org.junit.Test;

import java.util.Optional;

public class OptionalTest {

    @Test
    public void test1() {
        Optional<Integer> opt1 = Optional.empty();
        // true
        System.out.println(opt1.isEmpty());
        // NoSuchElementException: No value present
        System.out.println(opt1.get());
    }
}
