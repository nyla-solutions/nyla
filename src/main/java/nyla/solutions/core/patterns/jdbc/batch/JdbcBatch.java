package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.patterns.batch.BatchListExecutor;
import nyla.solutions.core.patterns.batch.BatchReport;
import nyla.solutions.core.patterns.jdbc.ResultSetToMapConverter;

import java.sql.ResultSet;
import java.util.Map;

public class JdbcBatch
{
    private final  BatchListExecutor<ResultSet, Map<String,?>> batchListExecutor;

    public JdbcBatch(ResultSetSupplier resultSetSupplier, ResultSetToMapConverter resultSetToMapConverter,
                     PreparedStatementMapConsumer preparedStatementMapConsumer, int batchChunkSize)
    {
        batchListExecutor = new BatchListExecutor<ResultSet, Map<String,?>>(resultSetSupplier,
                preparedStatementMapConsumer ,
                batchChunkSize,
                resultSetToMapConverter
                );
    }

    public BatchReport execute()
    {
        return batchListExecutor.execute();
    }
}
