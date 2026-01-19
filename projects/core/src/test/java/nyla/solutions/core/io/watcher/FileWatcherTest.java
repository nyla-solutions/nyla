package nyla.solutions.core.io.watcher;

import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class FileWatcherTest {

    private FileWatcher subject;
    private final Path path = Path.of("runtime/fileWatcher");
    private final String wildcard =  "*.txt";

    @BeforeEach
    void setUp() throws IOException {
        IO.dir().mkdir(path);
    }

    @Test
    void watchFile() throws IOException {

        AtomicBoolean  fileWasDetected = new AtomicBoolean(false);

        var watcher = FileWatcher.builder()
                .path(path)
                .wildcard(wildcard)
                .observer(file -> {
                    fileWasDetected.set(true);
                })
                .build();

        String fileName = "runtime/fileWatcher/text.txt";
        IO.writer().writeAppend(Paths.get(fileName).toFile(),"Testing file watcher\n");

        watcher.watchForFile();

        assertThat(fileWasDetected).isTrue();
    }

    @Test
    void given_directory_delete_when_watch_then_observer_not_called() throws IOException {
        AtomicBoolean  fileWasDetected = new AtomicBoolean(false);

        var watcher = FileWatcher.builder()
                .path(path)
                .wildcard(wildcard)
                .observer(file -> {
                    fileWasDetected.set(true);
                })
                .build();

        IO.dir().delete(path.toFile());

        watcher.watchForFileWithDuration(2, TimeUnit.SECONDS);

        assertThat(fileWasDetected).isFalse();
    }

    @Test
    void given_file_modified_then_observe_called() throws IOException {
        AtomicBoolean  fileWasDetected = new AtomicBoolean(false);

        var watcher = FileWatcher.builder()
                .path(path)
                .wildcard(wildcard)
                .observer(file -> {
                    fileWasDetected.set(true);
                })
                .build();

        String fileName = "runtime/fileWatcher/text.txt";
        IO.writer().writeAppend(Paths.get(fileName).toFile(),"Testing file watcher\n");
        watcher.watchForFile();

        fileWasDetected.set(false);
        IO.writer().touch(Paths.get(fileName).toFile());
        watcher.watchForFile();

        assertThat(fileWasDetected).isTrue();
    }

    @Test
    void given_file_doesNot_match_when_waitFor_then_observer_notCalled() throws IOException {

        AtomicBoolean  fileWasDetected = new AtomicBoolean(false);

        var watcher = FileWatcher.builder()
                .path(path)
                .wildcard(wildcard)
                .observer(file -> {
                    fileWasDetected.set(true);
                })
                .build();

        IO.writer().writeAppend(Paths.get("runtime/fileWatcher/text.ignored").toFile(),"Testing file watcher\n");

        watcher.watchForFileWithDuration(2, TimeUnit.SECONDS);

        assertThat(fileWasDetected).isFalse();
    }
}