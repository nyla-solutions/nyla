package nyla.solutions.core.patterns.decorator;

/**
 *
 * @param <ReturnType> the type to result
 * @param <InputType> the input type
 */
public interface Decorator<ReturnType,InputType> {
    ReturnType decorate(InputType input);
}
