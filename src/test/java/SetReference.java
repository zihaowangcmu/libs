import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SetReference {

    @Test
    public void setAsValueOfMap() {
        Map<String, Set<Integer>> map = new HashMap();
        String key = "test";
        Set<Integer> value = new HashSet<Integer>();
        map.put(key, value);

        assertEquals(map.get(key).size(), 0);

        // value add 0, and the map.get(key) is updated too
        value.add(0);

        assertEquals(map.get(key).size(), 1);
    }

    @Test
    public void changeTheValueFromGetter() {

    }

//    class A {
//
//        private B b;
//        public A(B b) {
//            this.b = b;
//        }
//
//        public B getB() {
//            return this.b;
//        }
//
//        public void setB(B b) {
//            this.b = b;
//        }
//    }
//
//    class B {
//
//        private C c;
//        public B(B b) {
//            this.b = b;
//        }
//
//        public B getB() {
//            return this.b;
//        }
//
//        public void setB(B b) {
//            this.b = b;
//        }
//    }

}
