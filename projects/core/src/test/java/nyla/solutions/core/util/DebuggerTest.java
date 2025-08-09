package nyla.solutions.core.util;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for Debugger
 * @author gregory green
 */
public class DebuggerTest
{

    @Test
    void prettyPrint() {

        var input = asList("1", "2");

        var actual = Debugger.toPrettyPrint(input);
        System.out.println(actual);

        assertThat(actual).contains("\n");
    }

    @Test
    void test_toString_objectArray() {

        String[] inputs = {"1","2"};

        var actual = Debugger.toString(inputs);

        Debugger.println(this,actual);


        assertThat(actual).contains("2");
        assertThat(actual).contains("1");
    }

    @Test
    void disableDebugger() {
        Debugger.debug(false);

        Debugger.println("Should not print");

        Debugger.debug(true);

        Debugger.println("Should print");


        Debugger.debug(false);

        Debugger.println(this,"Should not print");

        Debugger.debug(true);

        Debugger.println(this, "Should print");
    }

    @Test
    void test_toString_objectArrayComplex() {

        var creator = JavaBeanGeneratorCreator.of(UserProfile.class);

        UserProfile[] inputs = {creator.create(),creator.create()};

        var actual = Debugger.toString(inputs);
        Debugger.println(this,actual);

        assertThat(actual).contains(inputs[0].getEmail());
        assertThat(actual).contains(inputs[0].getFirstName());

    }

    @Test
    void dump() {
        Debugger.dump(JavaBeanGeneratorCreator.of(JavaBeanTest.ComplexObject.class));
    }

    @Test
    public void testFormattedMessages()
    throws Exception
    {
        Debugger.println(this, "Hello %s", "World");
        Debugger.printInfo(this, "Hello %s", "World");
        Debugger.printError(this, "Error Hello %s", "World");
        Debugger.printWarn(this, "Warn Hello %s", "World");
        Debugger.printFatal(this, "Fatal Hello %s", "World");
    }


    @Test
    void println_format() {
        Debugger.println(this,"This %s %s","Hello", "World");
    }

    @Test
    void printInfo_format() {
        Debugger.printInfo(this,"This Info %s %s","Hello", "World");
    }

    @Test
    void printError_format() {
        Debugger.printError(this,"This Error %s %s","Hello", "World");
    }

    @Test
    void printWarn_format() {
        Debugger.printWarn(this,"This Warn %s %s","Hello", "World");
    }


    @Test
    void printFatal_format() {
        Debugger.printFatal(this,"This Fatal %s %s","Hello", "World");
    }


    @Test
    void stackTrace_WhenArgNull_ReturnContainsWordNull()
    {
        Throwable t = null;
        String actual = Debugger.stackTrace(t);
        assertNotNull(actual);
    }

    @Test
    void stackTrace_WhenArgNull_ReturnContainsTestMethodName()
    {
        try
        {
            throw new Exception();
        }
        catch(Exception t)
        {
            String actual = Debugger.stackTrace(t);
            assertNotNull(actual);
            assertTrue(actual.contains("stackTrace_WhenArgNull_ReturnContainsTestMethodName"),"OUTPUT:"+actual);
        }
    }
    @Test
    public void printError()
    {
        Debugger.printError("THis is error message");
    }

    @Test
    public void printError_withClass()
    {
        Debugger.printError(this, "THis is error message");
    }

    @Test
    public void printFatal()
    {
        Debugger.printFatal("THis is fatal message");
    }

    @Test
    public void printFatal_withClass()
    {
        Debugger.printFatal(this, "THis is fatal message");
    }

    @Test
    public void printWarn()
    {
        Debugger.printWarn("THis is warn message");
    }

    @Test
    public void printWarn_withClass()
    {
        Debugger.printWarn(this, "THis is warn message");
    }

    @Test
    @SuppressWarnings("null")
    public void printInfo()
    {
        //The Debugger toString(Object) can be used to debug objects where the toString method is not implemented.
        String[] arraysNicely = {"first", "second"};
        String text = Debugger.toString(arraysNicely);
        assertEquals("{[0]=first ,[1]=second}", text);


        //The print method wraps log levels of DEBUG,INFO,WARN and FATAL
        Debugger.printInfo("This is a INFO level message");

        //Two arguments can be passed where the first is the calling object
        //The debugger will prepend the calling objects class name to the logged output
        Debugger.println(this, "This is a DEBUG level message");

        //Debugger can be used to efficiently print exception information
        text = null;
        try
        {
            text.toString(); //causes a null pointer exception
        }
        catch (NullPointerException e)
        {
            //Use the stackTrace method to get the string version of the exception call stack
            String stackTrace = Debugger.stackTrace(e);

            //Print warn level
            Debugger.printWarn(this, stackTrace);


            //stack trace will be automatically to created if the exception object is passed
            Debugger.printError(e);

            Debugger.printFatal(this, "Stack trace will not be printed, because this is not an exception object.");
        }
    }

}
