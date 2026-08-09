package nyla.solutions.core.util.organizer;

import nyla.solutions.core.exception.SystemException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FilterOrganizerTest {

    public static class TestBean {

        private String name;
        private String date;
        private int page;

        public TestBean(String name, String date, int page) {
            this.name = name;
            this.date = date;
            this.page = page;
        }

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }

        public int getPage() {
            return page;
        }
    }

    @Test
    void filterByJavaBeanProperty_shouldReturnMatchingObjects() {

        List<Object> list = List.of(
                new TestBean("John", "01/01/2024", 1),
                new TestBean("Mary", "01/02/2024", 2),
                new TestBean("John", "01/03/2024", 3)
        );

        Collection<Object> results =
                FilterOrganizer.filterByJavaBeanProperty(
                        list,
                        "name",
                        "John");

        assertEquals(2, results.size());
    }

    @Test
    void filterByJavaBeanProperty_shouldReturnEmptyCollectionWhenNoMatches() {

        List<Object> list = List.of(
                new TestBean("John", "01/01/2024", 1),
                new TestBean("Mary", "01/02/2024", 2)
        );

        Collection<Object> results =
                FilterOrganizer.filterByJavaBeanProperty(
                        list,
                        "name",
                        "Peter");

        assertTrue(results.isEmpty());
    }

    @Test
    void filterByJavaBeanProperty_shouldReturnEmptyCollectionForEmptyList() {

        List<Object> list = new ArrayList<>();

        Collection<Object> results =
                FilterOrganizer.filterByJavaBeanProperty(
                        list,
                        "name",
                        "John");

        assertTrue(results.isEmpty());
    }

    @Test
    void filterByJavaBeanProperty_shouldThrowSystemExceptionForNullList() {

        assertThrows(SystemException.class, () ->
                FilterOrganizer.filterByJavaBeanProperty(
                        null,
                        "name",
                        "John"));
    }

    @Test
    void filterByJavaBeanProperty_shouldThrowSystemExceptionForInvalidProperty() {

        List<Object> list = List.of(
                new TestBean("John", "01/01/2024", 1)
        );

        assertThrows(SystemException.class, () ->
                FilterOrganizer.filterByJavaBeanProperty(
                        list,
                        "unknownProperty",
                        "John"));
    }

    @Test
    void filterByJavaBeanDateProperty_shouldReturnObjectsWithinDateRange() {

        List<Object> list = List.of(
                new TestBean("John", "01/01/2024", 1),
                new TestBean("Mary", "01/05/2024", 2),
                new TestBean("Peter", "01/10/2024", 3)
        );

        Collection<Object> results =
                FilterOrganizer.filterByJavaBeanDateProperty(
                        list,
                        "date",
                        "01/02/2024",
                        "01/09/2024");

        assertEquals(1, results.size());
    }

    @Test
    void filterByJavaBeanDateProperty_shouldReturnEmptyCollectionWhenNothingMatches() {

        List<Object> list = List.of(
                new TestBean("John", "01/01/2024", 1)
        );

        Collection<Object> results =
                FilterOrganizer.filterByJavaBeanDateProperty(
                        list,
                        "date",
                        "01/02/2024",
                        "02/10/2024");

        assertTrue(results.isEmpty());
    }

    @Test
    void filterByJavaBeanDateProperty_shouldIgnoreBeansWithInvalidDates() {

        List<Object> list = List.of(
                new TestBean("John", "bad-date", 1),
                new TestBean("Mary", "1/5/2024", 2)
        );

        var actual = FilterOrganizer.filterByJavaBeanDateProperty(
                        list,
                        "date",
                        "01/01/2024",
                        "01/10/2024");

        assertThat(actual).isNotEmpty();
    }


    @Test
    void filterByJavaBeanDateProperty_shouldThrowSystemExceptionWhenListIsNull() {

        assertThrows(SystemException.class, () ->
                FilterOrganizer.filterByJavaBeanDateProperty(
                        null,
                        "date",
                        "01/01/2024",
                        "2024-01-10"));
    }

    @Test
    void filterByJavaBeanPageProperty_shouldReturnObjectsWithinRange() {

        ArrayList<Object> list = new ArrayList<>();

        list.add(new TestBean("John", "01/01/2024", 1));
        list.add(new TestBean("Mary", "01/02/2024", 5));
        list.add(new TestBean("Peter", "2024-01-03", 10));

        Collection<Object> results =
                FilterOrganizer.filterByJavaBeanPageProperty(
                        list,
                        "page",
                        2,
                        8);

        assertEquals(1, results.size());
    }

    @Test
    void filterByJavaBeanPageProperty_shouldIncludeBoundaryValues() {

        ArrayList<Object> list = new ArrayList<>();

        list.add(new TestBean("John", "01/01/2024", 2));
        list.add(new TestBean("Mary", "01/02/2024", 8));

        Collection<Object> results =
                FilterOrganizer.filterByJavaBeanPageProperty(
                        list,
                        "page",
                        2,
                        8);

        assertEquals(2, results.size());
    }

    @Test
    void filterByJavaBeanPageProperty_shouldReturnEmptyWhenNothingMatches() {

        ArrayList<Object> list = new ArrayList<>();

        list.add(new TestBean("John", "01/01/2024", 20));

        Collection<Object> results =
                FilterOrganizer.filterByJavaBeanPageProperty(
                        list,
                        "page",
                        1,
                        10);

        assertTrue(results.isEmpty());
    }

    @Test
    void filterByJavaBeanPageProperty_shouldThrowSystemExceptionForNullList() {

        assertThrows(SystemException.class, () ->
                FilterOrganizer.filterByJavaBeanPageProperty(
                        null,
                        "page",
                        1,
                        10));
    }

    @Test
    void filterByJavaBeanPageProperty_shouldIgnoreInvalidNumericValues() {

        ArrayList<Object> list = new ArrayList<>();

        list.add(new Object());

        Collection<Object> results =
                FilterOrganizer.filterByJavaBeanPageProperty(
                        list,
                        "page",
                        1,
                        10);

        assertTrue(results.isEmpty());
    }

}