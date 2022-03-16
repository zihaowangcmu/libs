package mock;

import model.Dog;
import model.Husky;
import model.Owner;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

//@RunWith(PowerMockRunner.class)
//@PowerMockIgnore("jdk.internal.reflect.*")
//@PrepareForTest({Dog.class, Husky.class})
public class MockVoidMethodTest {

    @Test
    public void testMockWorks() {
        Dog dogMock = mock(Dog.class);

        doAnswer(invocationOnMock -> {
            System.out.println("doAnswer succeeds!");
            return null;
        }).when(dogMock).bark();

        String fakeName = "Fake name";
        Dog dog = new Husky("TT", 2);
        when(dogMock.getName()).thenReturn(fakeName);
        dogMock.bark();
        System.out.println(dogMock.getName());

        doReturn(fakeName).when(dogMock).getName();
        System.out.println(dogMock.getName());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                System.out.println("doAnswer succeeds!");
                return null;
            }
        }).when(dogMock).bark();
        dogMock.bark();
    }

    @Test
    public void testSpyWorks() {
        String fakeName = "Fake name";
        Dog dog = new Husky("TT", 2);
        Dog dogSpy = spy(dog);

        when(dogSpy.getName()).thenReturn(fakeName);
        assertEquals(dogSpy.getName(), fakeName);

        doReturn(fakeName).when(dogSpy).getName();
        assertEquals(dogSpy.getName(), fakeName);

        doAnswer(invocationOnMock -> {
            System.out.println("doAnswer succeeds 1!");
            return null;
        }).when(dogSpy).bark();
        dogSpy.bark();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                System.out.println("doAnswer succeeds 2!");
                return null;
            }
        }).when(dogSpy).bark();
        dogSpy.bark();
    }

    @Test
    public void testSpy2() {
        Dog dog = new Husky("QQ", 1);
        dog = spy(dog);
        doAnswer(invocationOnMock -> {
            System.out.println("doAnswer succeeds 1!");
            return null;
        }).when(dog).bark();
        testBark(dog);
    }

    private void testBark(Dog dog) {
        dog.bark();
    }

    @Test
    public void testSpy3() {
        Owner owner = new Owner("zihao", 100);
        Dog dog = new Husky("HH", 1, owner);
        owner = spy(owner);
        Whitebox.setInternalState(dog, "owner", owner);
        doAnswer(invocationOnMock -> {
            System.out.println("This is a mocking introduce!");
            return null;
        }).when(owner).introduceMyself();
        dog.introduceOwner();
    }

    @Test
    public void testSpy4() {
        Owner owner = new Owner("zihao", 100);
        Dog dog = new Husky("HH", 1, owner);
        owner = spy(owner);
        Whitebox.setInternalState(dog, "owner", owner);
        doAnswer(invocationOnMock -> {
            System.out.println("This is a mocking sentence!");
            return null;
        }).when(owner).speak(any());
        dog.makeOwnerSpeak("I love my dog!");
    }
}
