package nyla.solutions.core.patterns.decorator;

/**
 * @author Gregory Green
 */
public abstract class TextDecoratorAdapter<T> implements TextDecorator<T>
{
    private T target;

    @Override
    public T getTarget()
    {
        return target;
    }

    /**
     * Set the target object to wrap
     * @param target the target
     */
    public void setTarget(T target)
    {
        this.target = target;
    }
}
