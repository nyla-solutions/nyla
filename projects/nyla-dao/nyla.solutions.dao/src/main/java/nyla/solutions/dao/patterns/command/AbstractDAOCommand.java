package nyla.solutions.dao.patterns.command;

import nyla.solutions.dao.DAO;

/**
 * 
 * @author Gregory Green
 * @param <I> the Input type
 * @param <O>  the Output type
 */
public abstract class AbstractDAOCommand<I,O> extends DAO implements DAOCommand<I,O>
{

}
