package nyla.solutions.core.patterns.memento;

import nyla.solutions.core.data.MethodCallFact;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SummaryException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.creational.proxy.ObjectProxy;
import nyla.solutions.core.util.Debugger;


/**
 * Handles replay method calls from a Memento
 * <p>
 * MethodCallObjectPreparer is used to modify an object prior to execute
 *
 * @author Gregory Green
 * @see Memento design pattern
 */
public class MethodCallSavePointPlayer<T>
{
    private MethodCallObjectPreparer methodCallObjectPreparer = null;
    private final T target;

    /**
     * @param target the target object
     */
    public MethodCallSavePointPlayer(T target)
    {
        if (target == null)
            throw new RequiredException("target");

        this.target = target;
    }// ----------------------------------------------

    /**
     * Redo the method calls of a target object
     *
     * @param memento    the memento to restore MethodCallFact
     * @param savePoints the list of the MethodCallFact save points
     * @throws SummaryException
     */
    public synchronized void playMethodCalls(Memento memento, String... savePoints)
    {
        String savePoint = null;

        MethodCallFact methodCallFact = null;
        SummaryException exceptions = new SummaryException();

        //loop thru savepoints
        for (int i = 0; i < savePoints.length; i++) {
            savePoint = savePoints[i];

            if (savePoint == null || savePoint.length() == 0 || savePoint.trim().length() == 0)
                continue;

            Debugger.println(this, "processing savepoint=" + savePoint);

            //get method call fact
            methodCallFact = memento.restore(savePoint, MethodCallFact.class);
            try {
                ObjectProxy.executeMethod(prepareObject(methodCallFact, savePoint), methodCallFact);
            }
            catch (Exception e) {
                exceptions.addException(new SystemException("savePoint=" + savePoint + " methodCallFact=" + methodCallFact + " exception=" + Debugger.stackTrace(e)));
                throw new SystemException(e); // TODO:
            }
        }

        if (!exceptions.isEmpty())
            throw exceptions;
    }// ----------------------------------------------

    /**
     * @param fact the method call fact
     * @return the prepared object by the the methodCallObjectPreparer
     */
    private Object prepareObject(MethodCallFact fact, String savePoint)
    {
        if (this.methodCallObjectPreparer != null)
            return methodCallObjectPreparer.prepare(this.target, fact, savePoint);

        //else return target
        return this.target;
    }// ----------------------------------------------

    /**
     * @return the target
     */
    public T getTarget()
    {
        return target;
    }


    /**
     * @return the methodCallObjectPreparer
     */
    public MethodCallObjectPreparer getMethodCallObjectPreparer()
    {
        return methodCallObjectPreparer;
    }// ----------------------------------------------

    /**
     * Set to modify an objet prior to execution
     *
     * @param methodCallObjectPreparer the methodCallObjectPreparer to set
     */
    public void setMethodCallObjectPreparer(
            MethodCallObjectPreparer methodCallObjectPreparer)
    {
        this.methodCallObjectPreparer = methodCallObjectPreparer;
    }// ----------------------------------------------

    public void record(String savePoint, MethodCallFact methodCallFact)
    {

    }
}
