package nyla.solutions.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebuggerTest
{

    @Test
    public void testFormattedMessages()
    throws Exception
    {
        Debugger.println(this, "Hello %s", "World");
        Thread.sleep(10);
        Debugger.printInfo(this, "Hello %s", "World");
        Thread.sleep(10);
        Debugger.printError(this, "Error Hello %s", "World");
        Thread.sleep(10);
        Debugger.printWarn(this, "Warn Hello %s", "World");
        Thread.sleep(10);
        Debugger.printFatal(this, "Fatal Hello %s", "World");
    }

    @Test
    @SuppressWarnings("null")
    public void testPrintlnObjectObject()
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
