package nyla.solutions.core.util.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextSplitterTest {

    private TextSplitter subject = new TextSplitter();

    @Test
    void splitToInts() {
        var actual = subject.splitToInts("");
    }
}