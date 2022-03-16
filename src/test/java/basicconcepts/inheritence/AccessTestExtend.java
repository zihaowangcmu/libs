package basicconcepts.inheritence;

public class AccessTestExtend extends AccessTest {

    public AccessTestExtend() {
        System.out.println("Extend class constructor!");
    }

    void method2() {
        // can call method1() in accessTest.class
        method1();
    }
}
