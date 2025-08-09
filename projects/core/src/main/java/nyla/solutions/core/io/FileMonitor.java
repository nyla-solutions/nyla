package nyla.solutions.core.io;

import nyla.solutions.core.patterns.observer.Topic;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import static nyla.solutions.core.util.Config.settings;

/**
 * Observable for the file watching
 */
public class FileMonitor extends Topic<FileEvent>
{
    public static final String TOPIC_NM = "FILE_MONITOR";

    /**
     * This is the timer thread which is executed every n milliseconds according
     * to the setting of the file monitor. It investigates the file in question
     * and notify listeners if changed.
     */
    private Timer timer;
    private final long pollingInterval;
    private final long delayMs;



    /**
     * Create a file monitor instance with specified polling interval.
     *
     * @param pollingInterval Polling interval in milliseconds.
     */
    public FileMonitor(long pollingInterval, long delayMs)
    {
        super(TOPIC_NM);

        this.pollingInterval = pollingInterval;
        this.delayMs = delayMs;

        timer = new Timer(true);

    }//------------------------------------------------------------------

    /**
     * Stop the file monitor polling.
     */
    public synchronized void stop()
    {
        timer.cancel();
    }//------------------------------------------------------------------

    /**
     * Used to wait for transferred file's content length to stop changes for
     * 5 seconds
     *
     * @param aFile the file
     */
    public static void waitFor(File aFile)
    {
        if (aFile == null)
            return;

        String path = aFile.getAbsolutePath();

        long previousSize = IO.getFileSize(path);
        long currentSize = previousSize;

        long sleepTime = settings().getPropertyLong("file.monitor.file.wait.time", 100).longValue();

        while (true)
        {
            try
            {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException e)
            {
            }

            currentSize = IO.getFileSize(path);

            if (currentSize == previousSize)
                return;

            previousSize = currentSize;
        }
    }//------------------------------------------------------------------

    /**
     * Add file to observer for. File may be any java.io.File (including a
     * directory) and may well be a non-existing file in the case where the
     * creating of the file is to be trepped.
     * <p>
     * More than one file can be listened for. When the specified file is
     * created, modified or deleted, listeners are notified.
     *
     * @param directory           the directory to listen for.
     * @param fileNameFilter      the file name
     * @param processCurrentFiles whether to process current names
     */
    public synchronized void monitor(String directory, String fileNameFilter, boolean processCurrentFiles)
    {

        timer.schedule(
                new FileMonitorNotifier(this,
                        directory,
                        fileNameFilter,
                        processCurrentFiles)
                , delayMs,
                pollingInterval);
    }//-------------------------------------------

    /**
     * Notify observers that the file has changed
     *
     * @param file the file that changes
     */
    protected synchronized void notifyChange(File file)
    {
        System.out.println("Notify change file=" + file.getAbsolutePath());

        this.notify(FileEvent.createChangedEvent(file));
    }//---------------------------------------------------

    /**
     * @param file file that was added
     */
    protected synchronized void notifyAdd(File file)
    {
        System.out.println("Notify added file=" + file.getAbsolutePath());

        this.notify(FileEvent.createAddedEvent(file));
    }//---------------------------------------------------

    protected long getDelayMs()
    {
        return delayMs;
    }
    private static class FileMonitorNotifier extends TimerTask
    {
        private Hashtable<File, Object> fileLastModifyTimeMap = new Hashtable<File, Object>(); // File -> Long
        private FilenameFilter filter = null;
        private File directory = null;
        /**
         * @uml.property name="fileMonitor"
         * @uml.associationEnd multiplicity="(0 1)"
         */
        private FileMonitor fileMonitor = null;


        /**
         * Constructor
         *
         * @param aFileMonitor        the file monitor
         * @param aDirectory          the directory to monitor
         * @param aFileFilter         the file filter example *.exe"
         * @param processCurrentFiles flag to process previous files
         */
        FileMonitorNotifier(FileMonitor aFileMonitor, String aDirectory,
                            String aFileFilter, boolean processCurrentFiles)
        {
            if (aDirectory == null || aDirectory.length() == 0)
                throw new IllegalArgumentException(
                        "Directory to minotor has not provided.");

            this.directory = new File(aDirectory);

            if (!this.directory.isDirectory())
                throw new IllegalArgumentException("Invalid directory \""
                        + aDirectory + "\" provided!");

            if (aFileMonitor == null)
                throw new IllegalArgumentException("FileMonitor not provided");

            this.fileMonitor = aFileMonitor;

            //----------------------------------------------
            this.filter = new WildCardFilter(aFileFilter);

            if (!processCurrentFiles)
                init();

        }//--------------------------------------

        private void init()
        {
            File[] files = this.directory.listFiles(this.filter);

            if (files == null)
                return;

            File file = null;
            for (int i = 0; i < files.length; i++)
            {
                file = files[i];
                this.fileLastModifyTimeMap.put(file, Long.valueOf(file.lastModified()));
            }
        }//-----------------------------------------

        /**
         * Process file change notifications
         */
        public void run()
        {
            try
            {
                // Loop over the registered files and see which have changed.
                // Use a copy of the list in case listener wants to alter the
                // list within its fileChanged method.

                if (this.directory == null)
                    return;

                File[] filesInDir = this.directory.listFiles(this.filter);

                if (filesInDir == null)
                    return;

                File dirfile = null;
                Long lastModifiedTime = null;
                long newModifiedTime = 0;

                //if in map and not in directory, then it was removed
                ArrayList<Object> processedFiles = new ArrayList<Object>();
                for (int i = 0; i < filesInDir.length; i++)
                {
                    dirfile = filesInDir[i];

                    if (processedFiles.contains(dirfile.getAbsolutePath()))
                        continue;

                    lastModifiedTime = (Long) fileLastModifyTimeMap.get(dirfile);

                    if (lastModifiedTime == null)
                    {
                        //file added
                        fileMonitor.notifyAdd(dirfile);
                    }
                    else
                    {
                        newModifiedTime = dirfile.exists() ? dirfile.lastModified() : -1;

                        // Check if file has changed
                        if (newModifiedTime != lastModifiedTime.longValue())
                        {
                            // Register new modified time
                            fileMonitor.notifyChange(dirfile);
                        }
                    }

                    fileLastModifyTimeMap.put(dirfile, Long.valueOf(dirfile.lastModified()));
                    processedFiles.add(dirfile.getAbsolutePath());
                }//end for loop

                List<File> filesInDirList = Arrays.asList(filesInDir);
                File fileInMap = null;

                Map.Entry<File, Object> mapEntry = null;

                for (Iterator<Map.Entry<File, Object>> i = this.fileLastModifyTimeMap.entrySet().iterator();
                     i.hasNext(); )
                {

                    try
                    {
                        mapEntry = i.next();
                        fileInMap = mapEntry.getKey();

                        if (!filesInDirList.contains(fileInMap))
                        {
                            System.out.println("File was removed " + fileInMap.getName());
                            //fileLastModifyTimeMap.remove(fileInMap);
                            i.remove();
                            System.out.println("DONE");
                        }
                    }
                    catch (ConcurrentModificationException e)
                    {
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }//---------------------------------------------------------------

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FileMonitor that = (FileMonitor) o;
        return pollingInterval == that.pollingInterval &&
                delayMs == that.delayMs &&
                Objects.equals(timer, that.timer);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), timer, pollingInterval, delayMs);
    }

    /**
     * @return Returns the pollingInterval.
     */
    public long getPollingInterval()
    {
        return pollingInterval;
    }
}

