package nyla.solutions.core.io.watcher;


import nyla.solutions.core.io.WildCardFilter;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import static java.nio.file.StandardWatchEventKinds.*;

/**
 * File Watcher monitors a directory for file changes based on a wildcard filter.
 * Register a consumer to process files.
 *
 * Example usage:
 * <pre>
 * AtomicBoolean  fileWasDetected = new AtomicBoolean(false);
 *
 *         var watcher = FileWatcher.builder()
 *                 .path(Paths.get("runtime/fileWatcher"))
 *                 .wildcard("*.txt)
 *                 .observer(file -> {
 *                     fileWasDetected.set(true);
 *                 })
 *                 .build();
 *
 *       // Wait indefinitely for a file event
 *         watcher.watchForFile();
 *
 *         // Or wait for a file event with a timeout
 *         watcher.watchForFileWithDuration(10, TimeUnit.SECONDS);
 *
 *         </pre>
 * @author Gregory Green
 */
public class FileWatcher implements Runnable {

    private final Path path;
    private final WildCardFilter wildCardFilter;
    private final WatchService watcher;
    private final Consumer<Path> fileObserver;

    /**
     * The file watcher constructor
     * @param path the directory path to monitor
     * @param wildcard the wildcard filter
     * @param fileObserver the file observer consumer
     */
    private FileWatcher(Path path, String wildcard, Consumer<Path> fileObserver) {
        this.path = path;
        this.wildCardFilter = new WildCardFilter(wildcard);
        this.fileObserver = fileObserver;
        try{
            this.watcher =  FileSystems.getDefault().newWatchService();
            this.path.register(watcher,
                    ENTRY_CREATE,
                    ENTRY_DELETE,
                    ENTRY_MODIFY);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Watches for a file event indefinitely
     */
    public void watchForFile() {

        try {
            var key = watcher.take();

            processWatchKey(key);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Watches for a file event for the given duration
     * @param time the time duration
     * @param timeUnit the time unit
     */
    public void watchForFileWithDuration(long time, TimeUnit timeUnit) {

        try {
            var key = watcher.poll(time, timeUnit);

            processWatchKey(key);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void watchForFileNoWait() {
            var key = watcher.poll();
            processWatchKey(key);
    }

    /**
     * The file watcher builder
     * @return the file watcher builder
     */
    public static FileWatchBuilder builder() {
        return new FileWatchBuilder();
    }

    @Override
    public void run() {
        this.watchForFileNoWait();
    }


    /**
     * The file watch builder class
     */
    public static class  FileWatchBuilder {
        private Path path;
        private String wildcard;
        private Consumer<Path> fileObserver;

        /**
         * Set the path
         * @param path the file
         * @return the builder
         */
        public FileWatchBuilder path(Path path) {
            this.path = path;
            return this;
        }

        /**
         * Set the wildcard
         * @param wildcard the wildcard
         * @return the builder
         */
        public FileWatchBuilder wildcard(String wildcard) {
            this.wildcard = wildcard;
            return this;
        }

        /**
         * Set the file observer
         * @param fileObserver the file observer
         * @return the builder
         */
        public FileWatchBuilder observer(Consumer<Path> fileObserver) {
            this.fileObserver = fileObserver;
            return this;
        }

        /**
         * Build the file watcher
         * @return the file watcher
         */
        public FileWatcher build() {
            return new FileWatcher(path, wildcard,fileObserver);
        }
    }


    private void processWatchKey(WatchKey key) {
        if(key == null)
            return; // no file changes

        for(var event : key.pollEvents()){
            var kind = event.kind();

            if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
                var watchEvent = (WatchEvent<Path>)event;
                if(!wildCardFilter.accept(path.toFile(), watchEvent.context().toString()))
                    continue;

                fileObserver.accept(watchEvent.context());
            }
        }
        key.reset();
    }
}
