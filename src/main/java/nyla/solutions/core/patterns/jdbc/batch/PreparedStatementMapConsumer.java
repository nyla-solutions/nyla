package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.exception.DataException;
import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.patterns.jdbc.BindVariableInterpreter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PreparedStatementMapConsumer implements Consumer<List<Map<String,?>>>
{
    private final BindVariableInterpreter bindVariableInterpreter;
    private final Creator<PreparedStatement> preparedStatementCreator;
    private final PreparedStatement preparedStatement;

    public PreparedStatementMapConsumer(BindVariableInterpreter bindVariableInterpreter,
                                        Creator<PreparedStatement> preparedStatementCreator)
    {
        this.bindVariableInterpreter = bindVariableInterpreter;
        this.preparedStatementCreator = preparedStatementCreator;
        PreparedStatement stmt = preparedStatementCreator.create();
        if(stmt == null)
        {
            throw new NullPointerException("The Creator object created a null prepared statement");
        }
        this.preparedStatement = stmt;
    }


    @Override
    public void accept(List<Map<String, ?>> maps)
    {
        maps.forEach( (map) -> {
            map.entrySet().forEach(e -> {
                Integer index = bindVariableInterpreter.indexOf(e.getKey());

                try {
                    preparedStatement.setObject(index,e.getValue());
                }
                catch (SQLException exception) {
                    throw new DataException("ERROR:"+exception.getMessage()+" input:"+e,exception);
                }
            });
            try {
                preparedStatement.addBatch();
            }
            catch (SQLException exception) {
                throw new DataException("ERROR:"+exception.getMessage()+" input:"+map,exception);
            }
        });

        try {
            preparedStatement.executeBatch();
        }
        catch (SQLException exception) {
            throw new DataException("ERROR:"+exception.getMessage()+" input:"+maps,exception);
        }

    }

    public void close()
    {
        try {
            this.preparedStatement.close();
        }
        catch (SQLException e) {
            throw new DataException(e.getMessage(),e);
        }
    }
}
