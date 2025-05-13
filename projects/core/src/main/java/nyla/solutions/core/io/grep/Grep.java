package nyla.solutions.core.io.grep;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.expression.BooleanExpression;
import nyla.solutions.core.util.Debugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * USing grep conventions to search for text in files
 * @author gregory green
 */
public class Grep {
    private final List<File> files;

    public Grep(File file) throws IOException {

        files = IO.dir().listFilesOnly(file);

    }

    public static Grep file(File file) throws IOException {
        return new Grep(file);
    }

    public GrepResult searchFirst(BooleanExpression<String> booleanExpression) throws IOException {

        for (File file : files)
        {
            var results = findFirst(booleanExpression,file);
            if(results != null)
                return new GrepResult(results,file);
        }

        return null;
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
}
