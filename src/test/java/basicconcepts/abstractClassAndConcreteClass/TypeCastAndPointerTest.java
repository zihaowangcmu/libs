package basicconcepts.abstractClassAndConcreteClass;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeCastAndPointerTest {

    @Test
    public void test() {
        DogInterface dog1 = new DogImp(1);
        DogImp dog2 = (DogImp) dog1;
        dog2.setAge(2);
        assertEquals(((DogImp) dog1).age, 2);
    }

    interface DogInterface {

    }

    class DogImp implements DogInterface {
         int age;
         public DogImp(int age) {
             this.age = age;
         }

         public void setAge(int age) {
             this.age = age;
         }
    }
}
