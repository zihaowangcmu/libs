package basicconcepts.exception;

import org.junit.Test;

public class UncheckedExceptionTest {

    @Test
    public void test1() {
        // unchecked-basic.exception no need to declared by method
        throw new NullPointerException("sss");
    }

    @Test
    public void test2() throws Exception {
        // must be declared by method
        throw new Exception("sss");
    }

    @Test
    public void test3() throws Exception {
        // unchecked-exceptions can be caught
        try {
            System.out.println("do something");
        } catch (RuntimeException re) {
            System.out.println("Caught unchecked-basic.exception");
        }
        throw new NullPointerException("sss");
    }

    @Test
    public void test4() throws Exception {
////         compile error
//        if ((1/2) && false) {
//            System.out.println("true");
//        }
//        else {
//            System.out.println("false");
//        }
    }

    @Test
    public void test5() throws Exception {
//        modPlusOneFilter(new ArrayList<>(), 10);
    }

//    List<Integer> modPlusOneFilter(List<Integer> input, int mod) {
//        mod++;
//        return input.stream()
//                // should be final or effectively final
//                .filter(element -> element % mod == 0)
//                .collect(Collectors.toList());
//
//    }



}
