package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.RequiredException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReplaceRegExpTextDecoratorTest
{

    @Test
    void getText_WhenNotTarget_ThenThrowException()
    {
        Textable textable = mock(Textable.class);
        ReplaceRegExpTextDecorator subject = new ReplaceRegExpTextDecorator();
        assertThrows(RequiredException.class,() -> subject.getText());
    }

    @Test
    void getText()
    {
        Textable textable = mock(Textable.class);
        ReplaceRegExpTextDecorator subject = new ReplaceRegExpTextDecorator(textable);
        String actual = subject.getText();
        verify(textable).getText();
    }
    @Test
    void getText_WhenRegExpReplacement_ThenReturnedExpectedResults()
    {
        String text = "Hello World Hello";
        Textable textable = new StringText(text);
        ReplaceRegExpTextDecorator subject = new ReplaceRegExpTextDecorator(textable);
        subject.setRegExp("^Hello");
        subject.setReplacement("Hi");
        String actual = subject.getText();
        assertEquals("Hi World Hello",actual);
    }

    @Test
    void getTarget()
    {
        String text = "Hello World Hello";
        Textable textable = new StringText(text);
        ReplaceRegExpTextDecorator subject = new ReplaceRegExpTextDecorator(textable);
        assertEquals(textable,subject.getTarget());
    }

    @Test
    void setTarget()
    {
        String text = "Hello World Hello";
        Textable target1 = new StringText(text);
        ReplaceRegExpTextDecorator subject = new ReplaceRegExpTextDecorator(target1);
        Textable target2 = new StringText(text+"2");
        subject.setTarget(target2);
        assertEquals(target2,subject.getTarget());
    }

    @Test
    void getReplacement_WhenSet_Then_Equals_Expected()
    {
        ReplaceRegExpTextDecorator subject = new ReplaceRegExpTextDecorator();
        String expected = "replacement";
        subject.setReplacement(expected);
        assertEquals(expected,subject.getReplacement());
    }


    @Test
    void getRegExp_When_Set_Then_Returns_Expected()
    {
        ReplaceRegExpTextDecorator subject = new ReplaceRegExpTextDecorator();
        String expected = "regExp";
        subject.setRegExp(expected);
        assertEquals(expected,subject.getRegExp());
    }

}