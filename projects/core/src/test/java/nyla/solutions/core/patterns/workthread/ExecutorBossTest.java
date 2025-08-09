package nyla.solutions.core.patterns.workthread;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExecutorBossTest
{

    @Test
    void startRunnable() throws ExecutionException, InterruptedException
    {
        Runnable runnable = null;

        int expectedSize = 3;
        int count = 3;
        ExecutorBoss subject = new ExecutorBoss(count);

        ArrayList<String> list = new ArrayList<>();

        Future<?> actual = subject.startWorking(() -> list.add(""));
        assertNotNull(actual);

        actual = subject.startWorking(() -> list.add(""));
        assertNotNull(actual);

        actual = subject.startWorking(() -> list.add(""));
        assertNotNull(actual);

        actual.get();

        assertEquals(expectedSize, list.size());

    }
}