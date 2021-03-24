package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.patterns.batch.BatchJob;
import nyla.solutions.core.patterns.batch.BatchReport;
import nyla.solutions.core.patterns.jdbc.ResultSetToMapConverter;

import java.sql.ResultSet;
import java.util.Map;

public class JdbcBatch
{
    private final BatchJob<ResultSet, Map<String,?>> batchJob;

    public JdbcBatch(ResultSetSupplier resultSetSupplier, ResultSetToMapConverter resultSetToMapConverter,
                     PreparedStatementMapConsumer preparedStatementMapConsumer, int batchChunkSize)
    {
        batchJob = new BatchJob<ResultSet, Map<String,?>>(resultSetSupplier,
                preparedStatementMapConsumer ,
                batchChunkSize,
                resultSetToMapConverter
                );
    }

    public BatchReport execute()
    {
        return batchJob.execute();
    }
}
