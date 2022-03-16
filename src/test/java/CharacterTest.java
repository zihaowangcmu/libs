import io.vertx.core.json.Json;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Character means only a character, so 'ab' is not a char!
 * 二进制 binary
 * 八进制 octal
 * 十进制 decimal
 * 十六进制 hexadecimal
 */
public class CharacterTest {

    private final Logger logger = LoggerFactory.getLogger(CharacterTest.class);

    /**
     * Diff between a lower case char and its upper case char is 32
     */
    @Test
    public void ASCIIValue() {
        int aInt = 'a';
        int AInt = 'A';
        logger.info("ASCII value of 'a' is " + aInt);
        logger.info("ASCII value of 'A' is " + AInt);
    }

    /**
     * Test cases for convert int to char
     * Compare these conditions:
     * 1. char b = (char) 1;
     * 2. char b = Character.forDigit(1, 10);
     * 3. Character.toChars(109);
     */
    @Test
    public void intToCharTest() {
        char b;

        // Get the char whose ascii value is given int.
        // For value being 1, it is start-of-heading char, which isn't printable.
        b = (char) 1;
        logger.info("char b = (char) 1 => " + b);

        // https://www.cs.cmu.edu/~pattis/15-1XX/common/handouts/ascii.html
        b = (char) 97;
        assertEquals(b, 'a');
        logger.info("char b = (char) 97 => " + b);

        // Convert an int digit (< 10) to char
        // here radix is 10, meaning decimal
        b = Character.forDigit(1, 10);
        assertEquals(b, '1');
        logger.info("b = Character.forDigit(1, 10) => " + b);

        // same as (char) 97
        char[] chars;
        chars = Character.toChars(97);
        assertEquals(chars[0], (char) 97);

        // Convert a int (more than 10) to char array
        chars = ("" + 109).toCharArray();
        assertEquals(chars[0], '1');
        assertEquals(chars[1], '0');
        assertEquals(chars[2], '9');
    }
}
