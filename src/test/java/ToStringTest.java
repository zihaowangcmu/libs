import org.junit.Test;

public class ToStringTest {

    class A {

        int val;

        public A(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "A: " + val;
        }
    }

    @Test
    public void test() {
        A a1 = new A(0);
        System.out.println(a1.toString());


    }
}
