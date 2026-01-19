package nyla.solutions.core.io.watcher;

import nyla.solutions.core.io.FileEvent;
import nyla.solutions.core.patterns.observer.SubjectObserver;

public interface FileObserver extends SubjectObserver<FileEvent> {
}
