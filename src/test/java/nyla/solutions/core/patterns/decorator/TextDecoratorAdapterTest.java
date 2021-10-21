package nyla.solutions.core.patterns.decorator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for TextDecoratorAdapter
 * @author  Gregory Green
 */
class TextDecoratorAdapterTest extends TextDecoratorAdapter<String>
{
    private String expected = "test";

    @Test
    void setTarget_When_GetTarget_ThenEqual()
    {
        this.setTarget(expected);
        assertEquals(expected,this.getTarget());

    }

    @Override
    public String getText()
    {
        return expected;
    }
}