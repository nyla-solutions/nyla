package nyla.solutions.core.io.grep;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.io.csv.CsvWriter;
import nyla.solutions.core.patterns.decorator.Decorator;

/**
 * Decorators a grep results in CSV format
 *
 * @author gregory green
 */
public class GrepResultCsvDecorator implements Decorator<String, GrepResult> {
    @Override
    public String decorate(GrepResult input) {
        return CsvWriter.toCSV(input.results(),input.file().getPath())+ IO.newline();
    }
}
