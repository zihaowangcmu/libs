package mock;

import model.Dog;
import model.Husky;
import model.Owner;
import org.junit.Test;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.notNull;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.testng.Assert.assertEquals;

public class WhenTest {

    @Test
    public void doAnswerWhenTest() {
        Dog dog = new Husky("DD", 1);
        dog = spy(dog);
        PowerMockito.when(dog.getName()).thenReturn("MockName");
        PowerMockito.when(dog.getAge()).thenReturn(1);
        System.out.println(dog.getName());
        System.out.println(dog.getAge());

        doAnswer(invocationOnMock -> {
            System.out.println("Mock grow()");
            return null;
        }).when(dog).grow(Matchers.anyInt());

        // First time got mocked.
        dog.grow(1);
        assertEquals(1, dog.getAge());

        // Second time also mocked.
        dog.grow(1);
        assertEquals(1, dog.getAge());
    }

    @Test
    public void test1() {
        Dog dog = new Husky("DD", 1);
        dog = spy(dog);

        when(dog.growAndGetAge(anyInt())).thenReturn(10);

//        dog.growAndGetAge(1);
//        assertEquals(dog.getAge(), 1);
//        assertEquals(dog.growAndGetAge(1), 10);
    }

    @Test
    public void test2() {
        Dog dog = new Husky("DD", 1);
        dog = spy(dog);

        doAnswer(invocationOnMock -> {
            System.out.println("Mock setOwner()");
            return null;
        }).when(dog).setOwner(any());

        // no basic.exception
    }

    @Test
    public void test3() {
        Dog dog = new Husky("DD", 1);
        dog = spy(dog);

        when(dog.setAndGetOwner(any())).thenReturn(new Owner("zw", 1));

        // Has basic.exception!
    }
}
