package nyla.solutions.core.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Gregory Green
 */
public class IoReader {

    /**
     *
     * @param filePath the file path
     * @return the file content
     * @throws IOException when IO exceptions occurs
     */
    public String readTextFile(Path filePath) throws IOException {
        if(filePath == null)
            return null;

        return Files.readString(filePath);
    }

    public List<String> readTextLines(Path path) throws IOException {

        return Files.readAllLines(path);
    }
}
