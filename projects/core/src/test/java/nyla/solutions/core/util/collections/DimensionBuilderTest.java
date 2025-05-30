package nyla.solutions.core.util.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for DimensionBuilder
 */
class DimensionBuilderTest {

    @BeforeEach
    void setUp() {
    }

    @DisplayName("Expected 10 dimensions, not 4")
    @Test
    void build() {

        Double[] inputs = {23.2,232.232,232.2,232.2};
        List<Double> expected = Arrays.asList(23.2,232.232,232.2,232.2,0.0,0.0,0.0,0.0,0.0,0.0);

        var subject = DimensionBuilder
                .builder(inputs)
                .fillValue(0.0)
                .length(10);

        List<Double> actual = subject.build();

        assertEquals(expected, actual);
    }

    @Test
    void build_ZeroValue() {

        Double[] inputs = {23.2};
        List<Double> expected = Arrays.asList(23.2);

        var subject = DimensionBuilder
                .builder(inputs);

        List<Double> actual = subject.build();

        assertEquals(expected, actual);
    }

    @Test
    void build_ZeroValue_Box() {

        double[] inputs = {23.2};
        List<Double> expected = Arrays.asList(23.2);

        var subject = DimensionBuilder
                .builder(inputs);

        List<Double> actual = subject.build();

        assertEquals(expected, actual);
    }

    @Test
    void fixed100() {

        Double[] inputs = {  -0.0025,   -0.0007,   -0.0121,    0.0118};

        var out = DimensionBuilder.builder(inputs).length(100)
                .fillValue(0.0).build();

        System.out.println(out);
    }

    @Test
    void build_length_LessThan_InputSize() {

        Double[] inputs = {23.2,232.232,232.2,232.2};
        List<Double> expected = Arrays.asList(23.2,232.232);

        var subject = DimensionBuilder
                .builder(inputs)
                .length(2);

        List<Double> actual = subject.build();

        assertEquals(expected, actual);
    }
}