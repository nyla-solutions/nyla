package nyla.solutions.core.patterns.jdbc.office;

import nyla.solutions.core.io.csv.CsvReader;
import nyla.solutions.core.patterns.creational.Creator;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CsvJdbcLoader
{
    public long load(Creator<Connection> creator, Reader csv, String sql, boolean hasHeader)
    throws SQLException, IOException
    {
        try (Connection connection = creator.create()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                return load(preparedStatement, csv, sql, hasHeader);
            }
        }
    }

    /**
     * @param csv          the CSV content
     * @param sql          insert columns must match column positions
     * @param hasHeaderRow
     * @return the loader of records
     *
     * @throws SQLException when SQL error occurs
     * @throws IOException  when IO error occurs
     */
    public long load(PreparedStatement preparedStatement, Reader csv, String sql, boolean hasHeaderRow)
    throws SQLException, IOException
    {
        CsvReader csvReader = new CsvReader(csv);
        int rowCnt = csvReader.size();

        long loadCnt =0;
        for (int row = 0; row < rowCnt; row++) {
            if (row == 0 && hasHeaderRow)
                continue;

            List<String> rowList = csvReader.row(row);
            for (int cols = 0; cols < rowList.size(); cols++) {
                preparedStatement.setObject(cols + 1, rowList.get(cols));
            }
            //preparedStatement.execute();
            preparedStatement.addBatch();
            loadCnt++;
        }

        preparedStatement.executeBatch();

        return loadCnt;

        //int[] counts = preparedStatement.executeBatch();
        //if(counts == null || counts.length == 0)
        //return 0;

        //            long summary =0 ;
        //            for (int i = 0; i < counts.length; i++) {
        //                summary += counts[i];
        //            }
        //            return summary;

    }
}
