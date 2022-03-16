package basicconcepts.inheritence;

import model.Dog;
import model.Husky;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DogTest {

    @Test
    public void getNameTest() {
        Dog dog = new Husky("chai", 1);
        assertEquals(dog.getName(), "chai");
        // Can't call getAge!
    }

    @Test
    public void getAgeTest() {
        Dog dog = new Husky("chai", 1);
        assertEquals(((Husky) dog).getAge(), 1);
        // Can't call getAge!
    }

//    @Test
//    public void accessTest() {
//        method1();
//    }

    void method3() {
        // can call default methods in the same package
        AccessTest accessTest = new AccessTest();
        accessTest.method1();
    }

    @Test
    public void test4() {
        AccessTestExtend accessTestExtend = new AccessTestExtend();
    }
}
