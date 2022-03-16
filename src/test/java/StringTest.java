import io.vertx.core.json.Json;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class StringTest {

    private Logger logger = LoggerFactory.getLogger(StringTest.class);

    @Test
    public void splitTest() {
        String podName = "tldb-stateful-set-2";
        String[] arr = podName.split("-");
        logger.info(Json.encodePrettily(arr));
        logger.info("last: {}", arr[arr.length - 1]);
    }

    @Test
    public void split2Test() {
        String schemaId = "wv::v1.json";
        String[] arr = schemaId.split(":");
        assertEquals(arr.length, 2);
        logger.info("size is {}", arr.length);
        logger.info(Json.encodePrettily(arr));
    }

    @Test
    public void split3Test() {
        String schemaId = "wv::";
        String[] arr = schemaId.split(":");
        assertEquals(arr.length, 1);
        logger.info("size is {}", arr.length);
        logger.info(Json.encodePrettily(arr));
    }

    @Test
    public void substringTest() {
        String s = "#/json";
        System.out.println(s.substring(1));
    }

    @Test
    public void decodeTest() {
        String IdentityToken = "TkFNRVNQQUNFLm1qYWlubG9jYWwudHJhY2VsaW5rLXNhY29tcGxpYW5jZQ==";
        String[] tokens = (new String(Base64.getDecoder().decode(IdentityToken))).split("\\.");
        System.out.println(tokens.length);
        System.out.println(Arrays.toString(tokens));
    }

    /**
     * String.contains()
     */
    @Test
    public void containsTest() {
        String s = "abcdefgad";
        String pattern = "ad";
        System.out.println(s.contains(pattern));
    }

    /**
     * String.indexOf()
     */
    @Test
    public void indexOfTest() {
        String s = "hhhl";
        System.out.println(s.indexOf('h', 3));
    }

    /**
     * String.lastIndexOf()
     */
    @Test
    public void lastIndexOfTest() {
        String s = "hhhl";
        // search from 3 backward
        System.out.println(s.lastIndexOf('h', 3));
    }
}
