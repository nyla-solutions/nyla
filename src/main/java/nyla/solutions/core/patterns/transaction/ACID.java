package nyla.solutions.core.patterns.transaction;

import nyla.solutions.core.patterns.Disposable;

public interface ACID extends Disposable
{

    public abstract void commit();

    public abstract void rollback();
}