package nyla.solutions.core.io;

import nyla.solutions.core.exception.RequiredException;

import java.io.File;

/**
 * @author Gregory Green
 */
public class IOFileOperation
{
    private final File file;

    public IOFileOperation(File file)
    {
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


}
