package nyla.solutions.core.io.grep;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.decorator.Decorator;
import nyla.solutions.core.patterns.decorator.TextDecorator;
import nyla.solutions.core.patterns.expression.BooleanExpression;
import nyla.solutions.core.util.Debugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * USing grep conventions to search for text in files
 * @author gregory green
 */
public class Grep {
    private final List<File> files;
    private final Decorator<String,GrepResult> defaultDecorator = new GrepResultCsvDecorator();

    /**
     * Create the grep instance
     * @param file the file to grep
     * @throws IOException with issues readingt file
     */
    public Grep(File file) throws IOException {
        files = IO.dir().listFilesOnly(file);

    }

    /**
     *
     * @param file the file or directory to grep
     * @return the instance
     * @throws IOException when an error occurs
     */
    public static Grep file(File file) throws IOException {
        return new Grep(file);
    }

    public static Grep file(String  file) throws IOException {
        return new Grep(Paths.get(file).toFile());
    }

    /**
     * Search for the first results of the matching boolean expression
     * @param booleanExpression the boolean expression for each line in files
     * @return the grep results with file and line that match
     * @throws IOException
     */
    public GrepResult searchFirst(BooleanExpression<String> booleanExpression) throws IOException {

        for (File file : files)
        {
            var results = findFirst(booleanExpression,file);
            if(results != null)
                return new GrepResult(results,file);
        }

        return null;
    }


    /**
     * Search to first number of the results
     * @param booleanExpression the boolean expression to test for each line in files
     * @param n the number of results
     * @return the list of grep results
     * @throws IOException when error reading or writing files
     */
    public List<GrepResult> searchFirstN(BooleanExpression<String> booleanExpression, int n) throws IOException {

        var results = new ArrayList<GrepResult>();

        for (File file : files) {
            try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (booleanExpression.apply(line)) {
                        results.add(new GrepResult(line, file));

                        if (results.size() >= n)
                            return results;
                    }
                }

            } catch (MalformedInputException e) {
                Debugger.printWarn("Skipping file:" + file.toPath());
            } catch (IOException | RuntimeException e) {
                throw new IOException("Cannot process file:" + file.toPath() + " ERROR:" + e.toString(), e);
            }
        }

        if (results.isEmpty())
            return null;

        return results;
    }

    /**
     * Search and write output the file
     * @param booleanExpression the boolean expression for each line in files
     * @param newFile the file to write
     * @return the grep created with the new file
     */
    public Grep searchToFile(BooleanExpression<String> booleanExpression, File newFile) throws IOException {
            return searchToFile(booleanExpression, newFile, defaultDecorator);
    }
        /**
         * Search and write output the file
         * @param booleanExpression the boolean expression for each line in files
         * @param newFile the file to write
         * @return the grep created with the new file
         */
    public Grep searchToFile(BooleanExpression<String> booleanExpression, File newFile, Decorator<String,GrepResult> decorator) throws IOException {

        for (File file : files) {
            try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (booleanExpression.apply(line)) {
                        IO.writeAppend(newFile, decorator.decorate(new GrepResult(line, file)));
                    }
                }

            } catch (MalformedInputException e) {
                Debugger.printWarn("Skipping file:" + file.toPath());
            } catch (IOException | RuntimeException e) {
                throw new IOException("Cannot process file:" + file.toPath() + " ERROR:" + e.toString(), e);
            }
        }
        return file(newFile);
    }



    private String findFirst(BooleanExpression<String> booleanExpression,File file) throws IOException {
        Debugger.println("Searching : "+file.getAbsolutePath());
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (booleanExpression.apply(line))
                    return line;
            }

            return null;
        }
        catch(MalformedInputException e)
        {
            Debugger.printWarn("Skipping file:"+file.toPath());
            return null;
        }
        catch(IOException | RuntimeException e )
        {
            throw new IOException("Cannot process file:"+file.toPath()+" ERROR:"+e.toString(),e);
        }
    }
}
