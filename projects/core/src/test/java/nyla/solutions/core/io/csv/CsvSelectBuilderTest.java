package nyla.solutions.core.io.csv;

import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nyla.solutions.core.util.Organizer.isEmpty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvSelectBuilderTest {

    private CsvReader reader;
    private CsvSelectBuilder subject;

    @BeforeEach
    void setUp() {
        reader = mock(CsvReader.class);
        subject = new CsvSelectBuilder(reader);
    }

    @Test
    void orderByWhenNegativeHeader_Then_doesNowThrowError() {

        var actual = subject.orderBy(-1).build();

        System.out.println(actual);
        assertNotNull(actual);
    }

    @Test
    void build() {

        List<List<String>> expected = Organizer.change().toList(
                Organizer.change().toList("a1","a2"),
                Organizer.change().toList("b1","b2"));

        when(this.reader.getData()).thenReturn(expected);

        var actual = subject.build();

        System.out.println(actual);
        assertEquals(expected, actual);
    }
    @Test
    void build_whenNoOrderByCalled_Then_returnEmpty() {

        assertTrue(isEmpty(subject.build()));
    }
}