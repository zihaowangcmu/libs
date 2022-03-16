package rxjava;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

public class SingleMergeTest {

    // Good
    @Test
    public void test1() throws InterruptedException {
        Single<RocksDbResponse> s1 = Single.just("")
                .observeOn(Schedulers.io())
                .flatMap(s -> getRocksResp())
                .doOnSuccess(x -> System.out.println("success RocksDbResponse on thread " + Thread.currentThread().getName()));
        Single<LuceneResponse> s2 = Single.just("")
                .observeOn(Schedulers.io())
                .flatMap(s -> getLuceneResp())
                .doOnSuccess(x -> System.out.println("success LuceneResponse on thread " + Thread.currentThread().getName()));
        Single.just("")
                .observeOn(Schedulers.io())
                .doOnSuccess(x -> System.out.println("test2 item on thread " + Thread.currentThread().getName()))
                .flatMap(s -> merge(s1, s2))
                .doOnSuccess(x -> System.out.println("test3 item on thread " + Thread.currentThread().getName()))
                .subscribe(res -> {
                    System.out.println(res);
                });
        System.out.println("Main thread is here, time=" + System.currentTimeMillis());
        Thread.sleep(10000);
    }

    private Single<WriteResponse> merge(Single<RocksDbResponse> s1, Single<LuceneResponse> s2) {
        return Single.merge(s1, s2)
                .collect(() -> new WriteResponse(), ((writeResponse, o) -> {
                    if (o instanceof RocksDbResponse) {
                        writeResponse.setRocksDbResponse((RocksDbResponse) o);
                    } else {
                        writeResponse.setLuceneResponse((LuceneResponse) o);
                    }
                }))
                .doOnSuccess(x -> System.out.println("merging item on thread " + Thread.currentThread().getName()));
    }

    // hugely bad
    // block main
    @Test
    public void test3() throws Exception {
        getMergeResponse()
                .subscribeOn(Schedulers.io())
                .subscribe(System.out::println);
        System.out.println("Main thread is here, time=" + System.currentTimeMillis());
        Thread.sleep(10000);
    }

    // bad
    @Test
    public void test4() throws Exception {
        Single.just("dummy1")
                .subscribeOn(Schedulers.io())
                .flatMap(s -> getMergeResponse())
                .subscribe( res -> {
                    System.out.println(res);
                });
        System.out.println("Main thread is here, time=" + System.currentTimeMillis());
        Thread.sleep(10000);
    }

    // Bad
    @Test
    public void test2() throws Exception{
        Single<WriteResponse> writeResponseSingle;
        Single.just("dummy1")
                .observeOn(Schedulers.io())
                .flatMap(s -> getMergeResponse())
                .subscribe( res -> {
                    System.out.println(res);
                });
//        Single.just("dummy2")
//                .observeOn(Schedulers.io())
//                .flatMap(s -> getMergeResponse())
//                .subscribe( res -> {
//                    System.out.println(res);
//                });

//        getMergeResponse()
//                .subscribe( res -> {
//                    System.out.println(res);
//                });
        System.out.println("Main thread is here, time=" + System.currentTimeMillis());
        Thread.sleep(10000);
    }

    private Single<WriteResponse> getMergeResponse() {
        return Single.merge(getRocksResp(), getLuceneResp())
//                .observeOn(Schedulers.io())
                .collect(() -> new WriteResponse(), (writeResponse, response) -> {
                    if (response instanceof RocksDbResponse) {
                        writeResponse.setRocksDbResponse((RocksDbResponse) response);
                    } else {
                        writeResponse.setLuceneResponse((LuceneResponse) response);
                    }
                });
    }

    private Single<RocksDbResponse> getRocksResp(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("processing item on thread " + Thread.currentThread().getName());
        System.out.println("RocksDbResponse is ready, time=" + System.currentTimeMillis());
        return Single.just(new RocksDbResponse("Rocks Done"));
    }
    private Single<LuceneResponse> getLuceneResp() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("processing item on thread " + Thread.currentThread().getName());
        System.out.println("LuceneResponse  is ready, time=" + System.currentTimeMillis());
        return Single.just(new LuceneResponse("Lucene Done"));
    }
    class WriteResponse {
        RocksDbResponse rocksDbResponse;
        LuceneResponse luceneResponse;
        @Override
        public String toString() {
            return "WriteResponse{" +
                    "rocksDbResponse=" + rocksDbResponse +
                    ", luceneResponse=" + luceneResponse +
                    '}';
        }
        public void setRocksDbResponse(RocksDbResponse rocksDbResponse) {
            this.rocksDbResponse = rocksDbResponse;
        }
        public void setLuceneResponse(LuceneResponse luceneResponse) {
            this.luceneResponse = luceneResponse;
        }
    }
    class RocksDbResponse {
        @Override
        public String toString() {
            return "RocksDbResponse{" +
                    "response='" + response + '\'' +
                    '}';
        }
        public RocksDbResponse(String response) {
            this.response = response;
        }
        String response;
    }
    class LuceneResponse {
        public LuceneResponse(String response) {
            this.response = response;
        }
        String response;
        @Override
        public String toString() {
            return "LuceneResponse{" +
                    "response='" + response + '\'' +
                    '}';
        }
    }
}

