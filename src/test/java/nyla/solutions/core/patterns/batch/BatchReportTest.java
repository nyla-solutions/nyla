package nyla.solutions.core.patterns.batch;

import static org.junit.jupiter.api.Assertions.*;
import  org.junit.jupiter.api.*;

class BatchReportTest
{
    @Test
    public void test_incrementInput()
    {
        BatchReport batchReport = new BatchReport();
        batchReport.incrementInput();

        assertEquals(1,batchReport.countInput());
    }

    @Test
    public void test_output_count()
    {
        BatchReport batchReport = new BatchReport();
        batchReport.incrementOutput(1);
        assertEquals(1,batchReport.countOutput());

        batchReport.incrementOutput(3);
        assertEquals(4,batchReport.countOutput());

    }

}