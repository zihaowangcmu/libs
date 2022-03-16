import com.tracelink.dnp.utils.functional.Try;
import org.junit.Test;

public class TLTryTest {

    @Test
    public void test() {
        Try<String> tryString = Try.failure(new Exception("Test"));
        System.out.println(tryString.getCause());
        System.out.println(tryString.getCause().getMessage());
    }
}
