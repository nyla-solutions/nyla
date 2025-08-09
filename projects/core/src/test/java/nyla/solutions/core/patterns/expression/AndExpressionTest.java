package nyla.solutions.core.patterns.expression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AndExpressionTest
{

    @Test
    void apply_WhenAllTrue_ThenReturnTrue()
    {
        AndExpression subject = new AndExpression(new TrueFalseBooleanExpression<String>(true),
                new TrueFalseBooleanExpression<String>(true),
                new TrueFalseBooleanExpression<String>(true));

        assertEquals(true,subject.apply("hello"));
    }

    @Test
    void apply_WhenOneFalse_ThenReturnFalse()
    {
        AndExpression subject = new AndExpression(new TrueFalseBooleanExpression<String>(true),
                new TrueFalseBooleanExpression<String>(true),
                new TrueFalseBooleanExpression<String>(false));

        assertEquals(false,subject.apply("hello"));
    }
}