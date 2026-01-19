package nyla.solutions.core.io;

import nyla.solutions.core.exception.RequiredException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author gregory green
 */
public class IoDir {

    protected IoDir(){}

    public List<File> listFilesOnly(String file) throws IOException {
        return listFilesOnly(Paths.get(file).toFile());
    }
    public List<File> listFilesOnly(File file) throws IOException {

        List<File> list = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(file.toPath())) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach( f -> {
                        list.add(f.toFile());
                    });
        }
        if(list.isEmpty())
            return null;

        return list;
    }

    /**
     * @param directory the directory to make
     * @throws IOException when an unexpected IO error occurs
     */
    public void mkdir(String directory)
            throws IOException
    {
        if (directory == null || directory.length() == 0)
            return;

        mkdir(Paths.get(directory).toFile());

    }

    /**
     * List the file under a given directory
     *
     * @param directory the directory
     * @param pattern   the file pattern
     * @return the list of files
     */
    public File[] listFiles(File directory, String pattern)
    {

        if (pattern == null || pattern.length() == 0)
            return null;

        validateDirectory(directory);

        // check for /
        int indexofSlash = pattern.indexOf("/");
        if (indexofSlash > 0)
        {
            // get text up still /
            String suffix = pattern.substring(0, indexofSlash);
            pattern = pattern.substring(indexofSlash + 1);
            // append directory
            directory = new File(directory.getAbsolutePath() + "/" + suffix);
            validateDirectory(directory);

        }

        WildCardFilter filter = createFilter(directory, pattern);

        return directory.listFiles(filter);
    }

    /**
     * Common function to build file list filter
     *
     * @param directory the directory to filter
     * @param pattern   the file patter (i.e. *.*)
     * @return the WildCard filter
     */
    private WildCardFilter createFilter(File directory, String pattern)
    {

        if (pattern == null || pattern.length() == 0)
            throw new IllegalArgumentException("pattern required in list");

        return new WildCardFilter(pattern);
    }



    private void validateDirectory(File directory)
    {
        if (directory == null)
            throw new RequiredException("directory in IO.list");

        if (!directory.exists())
            throw new IllegalArgumentException("Directory does not exist " + directory.getAbsolutePath());

        if (!directory.isDirectory())
        {
            throw new IllegalArgumentException("Must provide a directory " + directory.getAbsolutePath());
        }
    }

    /**
     * @param location the list path
     * @param pattern  the search patttern
     * @return array of files
     */
    public File[] listFiles(String location, String pattern)
    {
        return listFiles(new File(location), pattern);
    }

    public String[] list(String location, String pattern)
    {
        return list(new File(location), pattern);
    }

    /**
     * List nested files
     *
     * @param location the location of the top folder
     * @return the files
     */
    public File[] listFiles(String location)
    {
        if (location == null || location.length() == 0)
            throw new RequiredException("location in IO");

        File folder = new File(location);

        return listFiles(folder);
    }

    public File[] listFiles(File folder)
    {
        if (folder == null)
            return null;

        if (!folder.isDirectory())
            throw new RequiredException(folder.getAbsolutePath() + " is not a directory");

        return folder.listFiles();
    }

    /**
     * List the file under a given directory
     *
     * @param directory the directory
     * @param pattern   the file pattern
     * @return the list of file names
     */
    public String[] list(File directory, String pattern)
    {
        if (pattern == null)
            return directory.list();

        WildCardFilter filter = createFilter(directory, pattern);

        return directory.list(filter);
    }

    public Set<File> listFileRecursive(String dir, String pattern)
    {
        if (dir == null || dir.length() == 0)
            dir = ".";

        return listFileRecursive(Paths.get(dir).toFile(), pattern);
    }

    /**
     * @param folder the top folder
     * @return the nest folders in a director
     */
    public File[] listFolders(File folder)
    {

        if (folder == null)
            return null;

        if (!folder.isDirectory())
            throw new RequiredException(folder.getAbsolutePath() + " is not a folder");

        return folder.listFiles(new FolderFilter());
    }

    /**
     * List the file under a given directory
     *
     * @param directory the directory
     * @param pattern   the file pattern
     * @return the list of files
     */
    public Set<File> listFileRecursive(File directory, String pattern)
    {
        File[] files = listFiles(directory, pattern);

        Set<File> set = new HashSet<>(IO.FILE_IO_BATCH_SIZE);

        if (files != null && files.length > 0)
            set.addAll(Arrays.asList(files));

        //list directories

        File[] folders = listFolders(directory);


        if (folders != null && folders.length > 0)
        {

            for (File folder : folders)
            {

                Set<File> nested = listFileRecursive(folder, pattern);

                if (nested != null)
                    set.addAll(nested);

            }
        }

        if (set.isEmpty())
            return null;

        return set;
    }


    /**
     * Make a directory
     *
     * @param folder the folder/directory to create
     * @return true if the directory was created
     */
    public boolean mkdir(File folder)
    {

        if (folder == null || folder.exists())
            return false;

        // check if parent directory exists
        File parent = folder.getParentFile();
        if (parent != null && !parent.exists())
            mkdir(parent); // recursively check parentb

        return folder.mkdir();

    }

    /**
     * Delete a given the directory
     *
     * @param file the directory to delete
     */
    public boolean deleteFolder(File file)
            throws IOException
    {
        emptyFolder(file);

        return file.delete();
    }

    /**
     * Delete all files in a given folder
     *
     * @param directory the directory to empty
     * @throws IOException when an unknown IO error occurs
     */
    public void emptyFolder(File directory)
            throws IOException
    {
        File[] files = directory.listFiles();

        if (files != null && files.length > 0)
        {
            for (int i = 0; i < files.length; i++)
                delete(files[i]);
        }
    }


    /**
     * Delete the file o folder
     *
     * @param file the file/folder to delete
     * @return true if file as deleted
     * @throws IOException when an IO error occurs
     */
    public boolean delete(File file)
            throws IOException
    {
        if (file == null)
            throw new RequiredException("file");

        if (!file.exists())
            return false;

        if (file.isDirectory())
            return deleteFolder(file);
        else
        {
            return file.delete();

        }
    }

    /**
     * Make directory
     * @param path the path to make
     */
    public void mkdir(Path path) {
        mkdir(path.toFile());
    }
}
