package nyla.solutions.core.io;

import java.io.File;
import java.nio.file.Path;

/**
 * File operations for a given file
 * @author Gregory Green
 */
public class IOFileOperation
{
    private final File file;

    /**
     * 
     * @param file the file to perform operations on
     */
    public IOFileOperation(File file)
    {
        if(file == null)
            throw new NullPointerException("file provided is null");
        this.file = file;
    }

    public void deleteDirectoryFiles()
    {
        if(!this.file.isDirectory())
            return;

        File[] files = this.file.listFiles();
        if(files == null)
            return;

        for (File nestedFile: files)
        {
            nestedFile.delete();
        }
    }

    /**
     *
     * @return the created parent
     */
    public File mkParentDir()
    {
        Path parent = this.file.toPath().getParent();
        parent.toFile().mkdir();
        return parent.toFile();
    }
}
