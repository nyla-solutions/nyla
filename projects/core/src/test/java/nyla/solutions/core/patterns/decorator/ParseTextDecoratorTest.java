package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.RequiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParseTextDecoratorTest
{
    ParseTextDecorator subject;
    private Textable target = new StringText("0123456789");

    @BeforeEach
    void setUp()
    {
        subject = new ParseTextDecorator();
    }

    @Test
    public void test_parser_no_npe()
    {
        assertThrows(RequiredException.class, () -> subject.getText());

    }

    @Test
    public void test_parser_with_start_no_target()
    {
        subject.setStart("1");
        assertThrows(RequiredException.class, () -> subject.getText());
    }

    @Test
    public void test_parser_with_end_no_target()
    {
        subject.setEnd("9");
        assertThrows(RequiredException.class, () -> subject.getText());
    }

    @Test
    public void test_parser_with_target()
    {
        subject.setTarget(target);
        String actual = subject.getText();
        assertEquals(
                target.getText(),
                actual
        );
    }

    @Test
    public void test_parser_with_start_end_target()
    {
        subject.setTarget(target);
        subject.setStart("0");
        subject.setEnd("9");
        String actual = subject.getText();
        assertEquals(
                "12345678",
                actual
        );
    }

}