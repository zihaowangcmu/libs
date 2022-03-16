package basicconcepts.abstractClassAndConcreteClass;

import org.junit.Test;

public class TemplateDesignPatternTest {

    @Test
    public void templateDesignPatternTest() {
        ConcreteDog dog = new ConcreteDog();
        dog.bark();
    }

    abstract class abs {

        void bark() {
            System.out.println("WangWang");
            customizedBark();
        }

        void customizedBark() {
            // Do nothing for abs dog
        }
    }

    class ConcreteDog extends abs {

        @Override
        void customizedBark() {
            System.out.println("HAHAHAHAHAHAHA");
        }
    }

}
