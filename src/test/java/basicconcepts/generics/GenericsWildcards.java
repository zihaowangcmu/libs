package basicconcepts.generics;

import java.util.ArrayList;
import java.util.List;

public class GenericsWildcards {

    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<>();
        ints.add(3); ints.add(5); ints.add(10);
        double sum = sum(ints);
        System.out.println("Sum of ints="+sum);
    }

    /**
     * Note List<? extends Number> works, but List<Number> does not work!
     * Because List<Integer> is not a subtype of List<Number> !!!!!!
     * @param list
     * @return
     */
    public static double sum(List<? extends Number> list){
        double sum = 0;
        for(Number n : list){
            sum += n.doubleValue();
        }
        return sum;
    }

    /**
     * This will NOT work:
     *         List<Number> numbers = new ArrayList<Integer>();
     * This works:
     *         List<? extends Number> numbers = new ArrayList<Integer>();
     */
}

