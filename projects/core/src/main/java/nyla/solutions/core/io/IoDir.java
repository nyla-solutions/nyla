package nyla.solutions.core.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author gregory green
 */
public class IoDir {
    public List<File> listFilesOnly(String file) throws IOException {
        return listFilesOnly(Paths.get(file).toFile());
    }
    public List<File> listFilesOnly(File file) throws IOException {

        List<File> list = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(file.toPath())) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach( f -> {
                        list.add(f.toFile());
                    });
        }
        if(list.isEmpty())
            return null;

        return list;
    }
}
