package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.scripting.Scripting;
import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ScriptingTextTest
{
    private Scripting<String, String> scripting;
    private ScriptingText subject;

    @BeforeEach
    void setUp()
    {
        scripting = mock(Scripting.class);
        subject = new ScriptingText();
        subject.setScripting(scripting);
    }

    @Test
    void getText()
    {
        String expected = "hello";
        when(scripting.interpret(null, null)).thenReturn(expected);
        assertEquals(expected, subject.getText());
    }
    /*
    	if (this.scripting == null)
	   throw new RequiredException("this.scripting");


     */

    @Test
    void getText_WhenScriptingNull_ThenThrowRequiredException()
    {

        subject.setScripting(null);
        assertThrows(RequiredException.class, () -> subject.getText());
    }

    @Test
    void getText_WhenVariablesNotNull_ThenSetScriptingVariables()
    {
        subject.setVariables(Organizer.toMap("K","V"));
        subject.getText();
        verify(scripting).setVariables(subject.getVariables());
    }

    @Test
    void getText_WhenVariablesNull_ThenSetScriptingVariables()
    {
        subject.setVariables(null);
        subject.getText();
        verify(scripting,never()).setVariables(subject.getVariables());
    }


    @Test
    void getScripting_WhenSet_ThenReturnsExpected()
    {
        assertNotNull(subject.getScripting());
        subject.setScripting(null);
        assertNull(subject.getScripting());
    }


    @Test
    void getVariables_WhenSet_Then_Equals()
    {
        Map<String, ?> expected = Organizer.toMap("hello", "world");
        subject.setVariables(expected);

        assertEquals(expected, subject.getVariables());
    }

    @Test
    void getEvaluationObject_WhenSet_ThenEquals()
    {
        short expected = 1;
        subject.setEvaluationObject(expected);

        assertEquals(expected, subject.getEvaluationObject());
    }


    @Test
    void getExpression_WhenSet_ThenGet_Equals()
    {
        String expected = "hello";
        subject.setExpression(expected);
        assertEquals(expected, subject.getExpression());
    }

}