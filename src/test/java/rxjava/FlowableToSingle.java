package rxjava;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.BiConsumer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class FlowableToSingle {

    @Test
    public void test() {
        Flowable<Integer> f;
//        f = Flowable.just(1);
        f = Flowable.empty();
        Single<Integer> s;
        s = f.single(2);
        s = f.singleOrError();



//        Single<List<Integer>> singles = f.collect(new Callable<List<Integer>>() {
//            @Override
//            public List<Integer> call() {
//                return new ArrayList<Integer>();
//            }
//        }, new BiConsumer<List<Integer>, Integer>() {
//            @Override
//            public void accept(List<Integer> list, Integer v) {
//                list.add(v);
//            }
//        });

//        System.out.println(singles.blockingGet().size());
        System.out.println(s.blockingGet());
    }
}
