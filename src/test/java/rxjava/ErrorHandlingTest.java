package rxjava;

import io.reactivex.Observable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ErrorHandlingTest {

    @Before
    public void before() {}

    @After
    public void after() {}

    @Test
    public void doOnErrorTest() {
        Observable.error(new IOException("Something went wrong"))
                .doOnError(error -> System.err.println("The error message is: " + error.getMessage()))
                .subscribe(
                        x -> System.out.println("onNext should never be printed!"),
                        e -> {
                            System.err.println("In subscribe on error: ");
                            e.printStackTrace();
                        },
                        () -> System.out.println("onComplete should never be printed!"));
    }
}
