package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.Textable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MacroTextDecoratorTest
{
    MacroTextDecorator subject;
    Textable hello = () -> "Hello";
    Textable world = () -> "World";

    @BeforeEach
    public void setUp()
    {
        subject = new MacroTextDecorator();
    }

    @Test
    void getText_does_not_throw_npe()
    {
        assertTrue(subject.getText().length() == 0);

    }

    @Test
    void getText_with_target()
    {


        subject.setTarget(hello);
        assertEquals(hello.getText(), subject.getText());
    }

    @Test
    public void test_collections_no_target()
    {
        TextDecorator d1 = new ToStringTextDecorator("d1");

        TextDecorator d2 = new ToStringTextDecorator("d2");

        subject.setTextables(Arrays.asList(
                d1,
                d2
        ));

        assertEquals("d1d2", subject.getText());

    }

    @Test
    public void test_collections_a_target()
    {
        subject.setTarget(hello);

        TextDecorator decorator = new ToStringTextDecorator();

        subject.setTextables(Arrays.asList(
                decorator,
                decorator
        ));
        assertEquals("HelloHello", subject.getText());

    }

    @Test
    public void test_collections_no_target_with_seperator()
    {
        subject.setSeparator(" , ");

        TextDecorator decorator = new ToStringTextDecorator("Hello");
        TextDecorator people = new ToStringTextDecorator("People");

        subject.setTextables(Arrays.asList(
                decorator,
                people

        ));
        assertEquals("Hello , People", subject.getText());

    }

    @Test
    public void test_collections_target_with_seperator()
    {
        subject.setSeparator(" | ");
        subject.setTarget(hello);

        TextDecorator decorator = new ToStringTextDecorator();
        TextDecorator people = new ToStringTextDecorator("People");

        subject.setTextables(Arrays.asList(
                decorator,
                people

        ));
        assertEquals("Hello | People", subject.getText());

    }
}