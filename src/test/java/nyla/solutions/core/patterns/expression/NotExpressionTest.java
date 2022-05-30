package nyla.solutions.core.patterns.expression;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotExpressionTest
{
    @Test
    void apply()
    {
        ComparableExpression<BigDecimal> exp = ComparableExpression.greaterThan(BigDecimal.valueOf(35L));

        NotExpression<BigDecimal> subject = new NotExpression<>(exp);

        assertEquals(true,subject.apply(BigDecimal.valueOf(55)));
    }
}