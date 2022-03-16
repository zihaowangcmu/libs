package algorithm;

import org.junit.Test;

public class GCD {

    @Test
    public void test() {
        int i = gcdByEuclidsAlgorithm(4, 6);
        System.out.println(i);
    }

    int gcdByEuclidsAlgorithm(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, n1 % n2);
    }
}
