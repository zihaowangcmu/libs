import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class FileTest {

    @Test
    public void createFileTest() throws IOException {
        String filename = "test.json";
        File file = new File(filename);
        file.createNewFile(); // zwang-test/test.json
    }

    @Test
    public void readFileTest() throws IOException {
        String filename = "readFileTest.json";
        Path path = Path.of(filename);

//        // one way
//        File file = new File(filename);
//        file.createNewFile();

        Files.createFile(path);

        String content = "readFileTest blah blah...";

//        // one way
//        FileWriter myWriter = new FileWriter(filename);
//        myWriter.write("readFileTest blah blah...");
//        myWriter.close();

        Files.writeString(path, content);

        String read = Files.readString(path);
        assertEquals(read, content);

        Files.delete(path);
    }
}
