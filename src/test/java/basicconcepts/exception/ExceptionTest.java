package basicconcepts.exception;

import com.tracelink.dnp.utils.functional.Try;
import org.junit.Test;

public class ExceptionTest {

    @Test
    public void test() throws Exception {
        isValid(0).onFailure(e -> System.out.println(e))
                .onSuccess(s -> System.out.println("Success!"));
        System.out.println("See me?");
    }

    private Try<Boolean> isValid(int i) throws Exception {
        return Try.of(() -> {
                    if (i == 0) {
                        throw new Exception("Not valid");
                    } else {
                        return true;
                    }
                }
        );
    }
}
