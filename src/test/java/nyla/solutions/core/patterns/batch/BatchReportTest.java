package nyla.solutions.core.patterns.batch;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void when_populated_toString_has_values()
    {

        BatchReport batchReport = new JavaBeanGeneratorCreator<>(BatchReport.class)
                .randomizeAll().create();

        String expected = "{\"inputCount\":"+batchReport.countInput()
                +",\"outputCount\":"+batchReport.countOutput()+"}";
        String actual = batchReport.toString();
        assertEquals(expected,actual);

    }
}