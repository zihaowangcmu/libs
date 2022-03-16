package interview.amazon.oa1;

import io.vertx.core.json.Json;
import org.junit.Test;

import java.util.Arrays;

public class BoxWeight {

    @Test
    public void test() {
//        int[] boxes = new int[]{50,20,15,20,20};
//        int[] boxes = new int[]{2};
//        int[] boxes = new int[]{2, 2};
//        int[] boxes = new int[]{2, 2, 4};
        int[] boxes = new int[]{1,1,1,1,1,1};
        int[] res = solve(boxes);
        System.out.println(Json.encode(res));
    }

    private int[] solve(int[] boxes) {
        Arrays.sort(boxes);
        int l = -1, r = boxes.length;
        int d = 0; // long?
        while (l < r - 1) {
            if (d <= 0) {
                r--;
                d += boxes[r];
            } else {
                l++;
                d -= boxes[l];
            }
        }
        if (d <= 0) r--;
        int[] res = new int[boxes.length - r];
        for (int i = 0; i < res.length; i++) {
            res[i] = boxes[i + r];
        }
        return res;
    }
}
