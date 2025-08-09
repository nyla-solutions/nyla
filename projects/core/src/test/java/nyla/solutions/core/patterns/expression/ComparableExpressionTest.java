package nyla.solutions.core.patterns.expression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparableExpressionTest
{
    @Test
    void apply_WhenMatch_ThenEquals()
    {
        String value = "value";
        ComparableExpression<String> comparableExpression = new ComparableExpression<>(value,0);
        assertEquals(true,comparableExpression.apply(value));
    }

    @Test
    void constructor_WhenMatch_ThenEquals()
    {
        String value = "value";
        ComparableExpression<String> comparableExpression = ComparableExpression.equalsTo(value);
        assertEquals(true,comparableExpression.apply(value));
    }

    @Test
    void apply_WhenMatch_ThenNotEquals()
    {
        String value = "value";
        ComparableExpression<String> comparableExpression = new ComparableExpression<>(value+"1",0);
        assertEquals(false,comparableExpression.apply(value));
    }

    @Test
    void apply_GivenValueGreater_WhenGreater_ThenTrue()
    {
        Integer value = 85;
        ComparableExpression<Integer> comparableExpression = new ComparableExpression<>(value,1);
        assertEquals(true,comparableExpression.apply(95));
    }

    @Test
    void apply_GivenValueGreater_WhenLess_ThenFalse()
    {
        Double value = 85.0;
        ComparableExpression<Double> comparableExpression = new ComparableExpression<Double>(value,1);
        assertEquals(false,comparableExpression.apply(75.0));
    }

    @Test
    void apply_GivenValueLess_WhenLess_ThenTrue()
    {
        Double value = 85.0;
        ComparableExpression<Double> comparableExpression = new ComparableExpression<Double>(value,-1);
        assertEquals(true,comparableExpression.apply(75.0));
    }

    @Test
    void constructor_GivenValueGreater_WhenLess_ThenFalse()
    {
        Double value = 85.0;
        ComparableExpression<Double> comparableExpression =ComparableExpression.lessThan(value);
        assertEquals(false,comparableExpression.apply(105.0));
    }

    @Test
    void constructor_GivenValueLess_WhenLess_ThenTrue()
    {
        Double value = 85.0;
        ComparableExpression<Double> comparableExpression = ComparableExpression.lessThan(value);
        assertEquals(true,comparableExpression.apply(75.0));
    }

}