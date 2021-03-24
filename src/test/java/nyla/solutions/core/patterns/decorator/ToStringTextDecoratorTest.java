package nyla.solutions.core.patterns.decorator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ToStringTextDecoratorTest
{

    @Test
    void getText()
    {
        ToStringTextDecorator subject = new ToStringTextDecorator();
        assertNotNull(subject.getText());
    }

    @Test
    void getText_target()
    {
        ToStringTextDecorator subject = new ToStringTextDecorator();
        subject.setTarget("World");
        assertEquals("World",subject.getText());
    }
}