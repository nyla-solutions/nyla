package nyla.solutions.dao.patterns.command;

import nyla.solutions.commas.Command;
import nyla.solutions.core.patterns.Disposable;
import nyla.solutions.dao.Connectable;
import nyla.solutions.dao.DataSourceable;

public interface DAOCommand<ReturnType,InputType> extends Command<ReturnType,InputType>, DataSourceable, Connectable,
		Disposable
{

}
