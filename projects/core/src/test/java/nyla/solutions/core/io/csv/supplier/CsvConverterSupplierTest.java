package nyla.solutions.core.io.csv.supplier;

import nyla.solutions.core.io.csv.CsvReader;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CsvConverterSupplierTest {
    private Converter<List<String>, UserProfile> converter;
    private CsvReader reader;
    private CsvConverterSupplier<UserProfile> subject;


    @BeforeEach
    void setUp() {
        reader = mock(CsvReader.class);
        converter = mock(Converter.class);

    }

    @Test
    void given_csvReader_when_reader_return_converterRecord() {

        var list = asList(asList("email","first","last"));
        UserProfile expected = JavaBeanGeneratorCreator.of(UserProfile.class).create();

        when(reader.iterator()).thenReturn(list.iterator());
        when(converter.convert(any())).thenReturn(expected);

        subject = new CsvConverterSupplier<UserProfile>(reader,converter);

        var actual = subject.get();

        assertEquals(expected, actual);

        assertNull(subject.get());
    }

    @Test
    void given_skip_firstLine_when_get_then_skip() {

        UserProfile expected = JavaBeanGeneratorCreator.of(UserProfile.class).create();
        var list = asList(asList("header","header","header"),
                asList(expected.getEmail(),
                        expected.getLogin(),
                        expected.getFirstName(),
                        expected.getLastName()));


        Converter<List<String>, UserProfile> qaConverter = cols -> new UserProfile(
                cols.get(0), cols.get(1),cols.get(2),cols.get(3));

        subject = new CsvConverterSupplier<UserProfile>(list.iterator(),qaConverter);
        subject.skipLines(1);

        var actual = subject.get();

        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getLogin(), actual.getLogin());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());

    }
}