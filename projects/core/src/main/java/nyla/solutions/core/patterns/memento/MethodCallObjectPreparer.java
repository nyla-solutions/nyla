package nyla.solutions.core.patterns.memento;

import nyla.solutions.core.data.MethodCallFact;

public interface MethodCallObjectPreparer
{
   public <T> T prepare(T target, MethodCallFact methodCallFact, String savePoint);
}
