package rxjava;

import com.tracelink.dnp.utils.functional.Try;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ThreadUtils;

import java.util.Random;
import java.util.concurrent.ExecutorService;

public class MultiThreadTest {

    private Logger logger = LoggerFactory.getLogger(MultiThreadTest.class);

    @Test
    public void test() {
        Try<String> res = Try.failure(new Exception("sss"));
        res.onSuccess(x -> System.out.println("success"))
                .onFailure(x -> System.out.println("fail"));
        System.out.println("always?");
    }


    /**
     * Single thread
     * Completable
     * @throws InterruptedException
     */
    @Test
    public void test2() throws InterruptedException {
        printTime("Start");
        Completable c1 = Completable.fromRunnable(() -> printAfter(1000, "step1"));
        Completable c2 = Completable.fromRunnable(() -> printAfter(500, "step2"));
        Completable c3 = Completable.mergeArray(c1, c2).doOnComplete(() -> printTime("step3")).doOnError(e -> e.printStackTrace());
        c3.subscribe();
        printTime("Main thread");
        sleep(100);
    }

    private void print(String s) {
        System.out.println(s);
    }

    private void printAfter(int delay, String info) {
        sleep(delay);
        printTime(info);
    }

    private void printTime(String info) {
        System.out.println(info + ": " + System.currentTimeMillis());
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() throws InterruptedException {
        Single<String> s1 = Single.just("S1")
                .doOnSuccess(s -> System.out.println(s + " is success"))
                .doOnError(e -> e.printStackTrace());
        Single<String> s2 = Single.just("s2")
                .flatMap(s -> s1)
                .doOnSuccess(s -> System.out.println("Outer is success"))
                .doOnError(e -> e.printStackTrace());
        s2.subscribe();
        Thread.sleep(100);
        return;
    }

    /**
     * Good!!
     * Best!
     * MultiThread
     * Completable
     * @throws InterruptedException
     */
    @Test
    public void test4() throws InterruptedException {
        printTime("Start");
        Completable c1 = Completable.fromRunnable(() -> printAfter(1000, "step1 " + getThreadName())).subscribeOn(Schedulers.io());
        Completable c2 = Completable.fromRunnable(() -> printAfter(500, "step2 " + getThreadName())).subscribeOn(Schedulers.io());
        Completable c3 = Completable.mergeArray(c1, c2).doOnComplete(() -> printTime("step3 " + getThreadName())).doOnError(e -> e.printStackTrace()).subscribeOn(Schedulers.io());
        c3.blockingGet();
        printTime("Main thread");
    }

    /**
     * Bad
     */
    @Test
    public void test5() {
        printTime("Start");
        Single<Try<Void>> s1 = Single.just(writeToRDB()).subscribeOn(Schedulers.io());
        Single<Try<Void>> s2 = Single.just(writeToLucene()).subscribeOn(Schedulers.io());
        Single.merge(s1, s2)
                .doOnComplete(() -> printTime("Merge"))
                .doOnError(e -> e.printStackTrace())
                .subscribeOn(Schedulers.io())
                .subscribe();
        printTime("Main thread");
        sleep(2000);
    }

    /**
     * ?????
     * Not working
     */
    @Test
    public void test6() {
        printTime("Start");

        Observable.just(writeToRDB(), writeToLucene())
                .flatMap(tryVoid -> Observable.just(tryVoid).subscribeOn(Schedulers.io()))
                .subscribe();

        printTime("Main thread");
        sleep(2000);
    }

    /**
     * Have to use flatMap at outside(why?) or it won't work
     * This works
     */
    @Test
    public void test9() {
        printTime("Start");

        Observable.just(1000, 500)
                .flatMap(num ->
                        Observable.just(num)
                                .subscribeOn(Schedulers.io())
                                .map(i -> testWithSleep(i, "dummy")))
                .subscribe();

        printTime("Main thread");
        sleep(2000);
    }

    private Observable<Try<Void>> testWithSleep(int i, String s) {
        return Observable.just(
                Try.run(() -> {
                    printAfter(i, s + " " + getThreadName());
                }).mapException(e -> new Exception("fails. ", e)));
    }

    /**
     * Similar to test9
     * Use blockingSubscribe()
     * This works! Good!
     */
    @Test
    public void test10() {
        printTime("Start");

        Observable.just(1000, 500)
                .flatMap(num ->
                        Observable.just(num)
                                .subscribeOn(Schedulers.io())
                                .map(i -> testWithSleep(i, "dummy")))
                .blockingSubscribe();

        printTime("Main thread");
    }

    /**
     * Fail!
     * No flatMap outside
     */
    @Test
    public void test14() {
        printTime("Start");

        Observable.just(1000, 500)
                .subscribeOn(Schedulers.io())
                .map(i -> testWithSleep(i, "dummy"))
                .blockingSubscribe();

        printTime("Main thread");
    }

    /**
     * Test what will happen when refer to same obj
     * Good
     */
    @Test
    public void test11() {
        printTime("Start");
        Test11Object test11Object = new Test11Object(1000);

        Observable.just(test11Object, test11Object)
                .flatMap(obj -> Observable.just(obj).subscribeOn(Schedulers.io()).map(o -> testWithSleep(o, "dummy")))
                .blockingSubscribe();

        printTime("Main thread");
    }

    class Test11Object {
        public int time;
        public Test11Object(int time) {
            this.time = time;
        }
    }

    private Observable<Try<Void>> testWithSleep(Test11Object test11Object, String name) {
        return testWithSleep(test11Object.time, name);
    }

//    /**
//     * Fail, all on main
//     */
//    @Test
//    public void test12() {
//        RocksDbAndLuceneWriteResult result = generateResult();
//        result.getWriteToLuceneObservable().doOnError(e -> {
//            logger.error("Failed to commit transaction for Lucene, will shutdown.", e);
//            System.exit(1234);
//        });
//        result.getWriteToRocksDbObservable().doOnError(e -> {
//            logger.error("Failed to commit transaction for RocksDb, will shutdown.", e);
//            System.exit(1234);
//        });
//        printTime("Start");
//        Observable.just(result.getWriteToLuceneObservable(), result.getWriteToRocksDbObservable())
//                .flatMap(observable -> observable.subscribeOn(Schedulers.io()))
//                .blockingSubscribe();
//        printTime("End");
//    }

//    private RocksDbAndLuceneWriteResult generateResult() {
//        Observable<Try<Void>> ob1 = Observable.just(writeToRDB());
//        Observable<Try<Void>> ob2 = Observable.just(writeToLucene());
//        RocksDbAndLuceneWriteResult result = new RocksDbAndLuceneWriteResult(ob1, ob2);
//        return result;
//    }

    /**
     * Success
     * Best for now
     * test instanceof
     * test cast type
     * doOnComplete only show once(complete all)
     */
    @Test
    public void test13() {
        Test13Object t = new Test13Object(10, "test13");
        printTime("Start");
        Observable.just(t.time, t.name)
                .flatMap(v -> Observable.just(v).subscribeOn(Schedulers.io()).map(val -> {
                    if (val instanceof Integer) {
                        print("INT: " + val);
                        return writeToRDB((Integer) val);
                    } else {
                        print("String: " + val);
                        return writeToLucene();
                    }
                }))
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .blockingSubscribe();
        printTime("End");
    }

    class Test13Object {
        public int time;
        public String name;
        public Test13Object(int time, String name) {
            this.time = time;
            this.name = name;
        }
    }

    private Try<Void> writeToRDB(int time) {
        return writeToRDB();
    }

    /**
     * Good
     */
    @Test
    public void test7() {
        printTime("Start");
        Observable.just("l", "lo", "lon")
                .flatMap(v ->
                        Observable.just(v)
//                                .doOnNext(s -> System.out.println("processing item on thread " + Thread.currentThread().getName()))
                                .subscribeOn(Schedulers.computation())
                                .map(vv -> performLongOperation(vv)))
                .subscribe(length -> System.out.println("received item length " + length + " on thread " + Thread.currentThread().getName()));
        printTime("Main thread");

        sleep(10000);
    }

    /**
     * Returns length of each param wrapped into an Observable.
     */
    protected Observable<Integer> performLongOperation(String v) {
        Random random = new Random();
        try {
            Thread.sleep(1000);
            printTime(v);
            return Observable.just(v.length());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void test8() {
        printTime("Start");
        Observable<Integer> vals = Observable.range(1,10);

        vals.flatMap(val -> Observable.just(val)
                .subscribeOn(Schedulers.computation())
                .map(i -> intenseCalculation(i))
        ).subscribe(val -> System.out.println(val));

        printTime("Main");
        sleep(10000);
    }

    /**
     * SubscribeOn(Schedulers.from(ExecutorService.class))
     *
     * Works.
     */
    @Test
    public void test15() {
        ExecutorService executorService = ThreadUtils.initThread("my-thread-%d", 30);

        printTime("Start");

        Observable.just(1000, 500)
                .flatMap(num ->
                        Observable.just(num)
                                .subscribeOn(Schedulers.from(executorService))
                                .map(i -> testWithSleep(i, "dummy")))
                .blockingSubscribe();

        printTime("Main thread");
    }

    private Observable<Integer> intenseCalculation(int i) {
        writeToRDB();
        return Observable.just(i);
    }


    private Try<Void> writeToRDB() {
        return Try.run(() -> {
            printAfter(1000, "RDB on " + getThreadName());
        }).mapException(
                ex -> new Exception("Database in inconsistent state " +
                        "due to basic.exception during commit. Shutting down.", ex)
        );
    }

    private Try<Void> writeToLucene() {
        return Try.run(() -> {
            printAfter(500, "Lucene on " + getThreadName());
        }).mapException(
                ex -> new Exception("Database in inconsistent state " +
                        "due to basic.exception during commit. Shutting down.", ex)
        );
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }
}
