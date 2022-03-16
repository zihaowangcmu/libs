import org.junit.Test;

public class TryCatchTest {

    @Test
    public void test1() throws InterruptedException {
        long intervalMS = 300;
        long lastUpdatedTimeMS = System.currentTimeMillis();
        while (true) {
            try {
                if (System.currentTimeMillis() - lastUpdatedTimeMS < intervalMS) {
                    continue;
                }
                System.out.println("Do something.");
                lastUpdatedTimeMS = System.currentTimeMillis();
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            } finally {
                System.out.println("Finally!");
                Thread.sleep(100);
            }
        }
//        System.out.println("End of test");
    }
}
