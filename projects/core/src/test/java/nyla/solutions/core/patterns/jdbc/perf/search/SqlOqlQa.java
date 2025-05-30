package nyla.solutions.core.patterns.jdbc.perf.search;

import nyla.solutions.core.operations.performance.BenchMarker;
import nyla.solutions.core.operations.performance.PerformanceCheck;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.patterns.jdbc.Sql;
import nyla.solutions.core.patterns.jdbc.perf.search.domain.SearchAccount;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;

import java.sql.SQLException;

public class SqlOqlQa {

    private static Long loopCount = 1000L;
    private static Long recordCount = 1000000L;
    private static String driver = Config.settings().getProperty("driver", "org.postgresql.Driver");
    private static String connectionUrl = Config.settings().getProperty("connectionUrl", "jdbc:postgresql://localhost/postgres");
    private static String user = Config.settings().getProperty("user", "postgres");
    private static char[] password = Config.settings().getPropertyPassword("password", "");
    private static String sqlText = """
            select * from perf_search.accounts where code1 = 444
            AND startRange <= '2340236472000000123'
            ANd endRange >= '2340236472000000123'
            AND  code3= '33333' 
            """;

    private static String createSql = """
            CREATE SCHEMA IF NOT EXISTS perf_search;
            create table if not exists perf_search.accounts 
            (code1 integer not null PRIMARY KEY,
            code2 text not null,
            code3  integer not null,
            startRange text,
            endRange text,
            output smallint
            );
            """;

    /*
     INSERT INTO perf_search.accounts (code1, code2, code3, startRange,endRange,output)
            VALUES (1, ':code2', 3, ':startRange',':endRange', 0)
            ON CONFLICT(code1)
            DO UPDATE SET
              code2 = ':code2',
              code3 = 3;
     */


        private final static String createIndex = """
                CREATE INDEX IF NOT EXISTS code1 
                        ON perf_search.accounts (code1);
                        
                CREATE INDEX IF NOT EXISTS code2 
                        ON perf_search.accounts (code2);
                CREATE INDEX IF NOT EXISTS startRange 
                        ON perf_search.accounts (startRange);
                        
                CREATE INDEX IF NOT EXISTS endRange 
                        ON perf_search.accounts (endRange);
                """;

    /*


     */

    private static String upsertSql = """
            INSERT INTO perf_search.accounts (code1, code2, code3, startRange,endRange,output)
            VALUES (:code1, :code2, :code3, :startRange,:endRange, :output)
            ON CONFLICT(code1)
            DO UPDATE SET
              code2 = :code2 ,
              code3 = :code3
            """;

    private static JavaBeanGeneratorCreator<SearchAccount> creator = JavaBeanGeneratorCreator.of(SearchAccount.class);
    private static int capacity = 1000;

    public static void main(String[] args) throws InterruptedException, SQLException {

        var benchMark = BenchMarker.builder().loopCount(loopCount)
                .threadCount(1)
                .loopCount(loopCount)
                .threadSleepMs(10)
                .build();

        var sql = new Sql();

        var connection = Sql.createConnection(driver, connectionUrl, user, password);

        //setup
        sql.execute(connection, createSql);

        sql.execute(connection, createIndex);

//        for(int i=0;i< recordCount; i++)
//        {
//            var entry = creator.create();
//            entry.setCode1(i);
//
//            sql.executeUpdateSqlWithJavaBean(connection, upsertSql,entry
//
//            );
//
//        }



        var pertTest = new PerformanceCheck(benchMark, capacity);
        pertTest.perfCheck(() -> {
                    try {
                        sql.queryForMap(connection, sqlText);
                    } catch (Exception e) {
                        Debugger.printError(e);
                    }
                }

        );

//        Thread.sleep(1000*60);
        System.out.println(pertTest.getReport());

    }
}
