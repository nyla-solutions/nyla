package nyla.solutions.core.io.watcher;

import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.Assertions;
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
    private AtomicBoolean  fileWasDetected = new AtomicBoolean(false);

    @BeforeEach
    void setUp() throws IOException {
        IO.dir().mkdir(path);

        subject = FileWatcher.builder()
                .path(path)
                .wildcard(wildcard)
                .observer(file -> {
                    System.out.println("File detected: "+file);
                    fileWasDetected.set(true);
                })
                .build();
    }


    @Test
    void watchFile() throws IOException {


        String fileName = "runtime/fileWatcher/text.txt";
        IO.writer().writeAppend(Paths.get(fileName).toFile(),"Testing file watcher\n");

        subject.watchForFile();

        assertThat(fileWasDetected).isTrue();
    }

    @Test
    void given_directory_delete_when_watch_then_observer_not_called() throws IOException {

        IO.dir().delete(path.toFile());

        subject.watchForFileWithDuration(2, TimeUnit.SECONDS);

        assertThat(fileWasDetected).isFalse();
    }

    @Test
    void given_file_modified_then_observe_called() throws IOException {

        String fileName = "runtime/fileWatcher/text.txt";
        IO.writer().writeAppend(Paths.get(fileName).toFile(),"Testing file watcher\n");
        subject.watchForFile();

        fileWasDetected.set(false);
        IO.writer().touch(Paths.get(fileName).toFile());
        subject.watchForFile();

        assertThat(fileWasDetected).isTrue();
    }

    @Test
    void given_file_doesNot_match_when_waitFor_then_observer_notCalled() throws IOException {


        IO.writer().writeAppend(Paths.get("runtime/fileWatcher/text.ignored").toFile(),"Testing file watcher\n");

        subject.watchForFileWithDuration(2, TimeUnit.SECONDS);

        assertThat(fileWasDetected).isFalse();
    }

    @Test
    void given_no_files_when_watchForFileNoWait_then_do_not_wait() {

        Assertions.assertDoesNotThrow(() -> subject.watchForFileNoWait());
    }

    @Test
    void given_no_files_when_run_then_do_not_wait() {

        Assertions.assertDoesNotThrow(() -> subject.run());
    }

    @Test
    void given_new_file_when_run_call_observer() throws IOException, InterruptedException {

        String fileName = "runtime/fileWatcher/text.txt";
        IO.writer().writeAppend(Paths.get(fileName).toFile(),"Testing file watcher\n");
        subject.run();

        fileWasDetected.set(false);
        IO.writer().touch(Paths.get(fileName).toFile());

        Thread.sleep(1000);
        subject.run();
        Thread.sleep(1000);

        assertThat(fileWasDetected).isTrue();
    }
}