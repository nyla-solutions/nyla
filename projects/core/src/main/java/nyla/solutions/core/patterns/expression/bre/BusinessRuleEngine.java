package nyla.solutions.core.patterns.expression.bre;

import nyla.solutions.core.exception.RequiredException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A business rules engine is a software system that executes one or more function rules in at runtime.
 * The Business Rule Engineer supports chaining Function calls to be executed based on a logical rule name.
 *
 * Type parameters:
 * <T> – the type of the input to the function
 * <R> – the type of the result of the function
 * @author Gregory Green
 */
public class BusinessRuleEngine<T, R>
{
    private final Map<String, Function<T, R>> rules;

    public BusinessRuleEngine(Map<String, Function<T, R>> rules)
    {
        if(rules == null || rules.isEmpty())
            throw new RequiredException("Map of rules");

        this.rules = rules;
    }

    public static BreBuilder builder()
    {
        return new BreBuilder();
    }

    /**
     * Execute rule with a given name to return the results
     * @param ruleName the rule to apply
     * @param value the value to given to rule
     * @return the return value of the rules
     */
    public R applyForRule(String ruleName, T value)
    {
        Function<T,R> rule = this.rules.get(ruleName);
        if(rule == null)
            return null;

        return rule.apply(value);
    }

    /**
     * BRE builder
     * @param <T> the input expression type
     * @param <R> the return type
     */
    public static  class BreBuilder<T, R>
    {
        private Map<String, Function<T, R>> rules = new HashMap<>();
        private BreBuilder()
        {}

        public BreBuilder rule(String ruleName, Function<T, R> rule)
        {
            rules.put(ruleName,rule);
            return this;
        }

        public BusinessRuleEngine<T, R> build()
        {
            return new BusinessRuleEngine<>(rules);
        }
    }
}
