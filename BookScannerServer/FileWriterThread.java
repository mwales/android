import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

public class FileWriterThread extends Thread implements DataSink
{
    /// This number will be appended to the filename
    private int mSuffixNumber = 0;
    
    /// Beginning part of the filename to write
    private String mFilenamePrefix;
    
    private String mExtension;
    
    private boolean mRunFlag = true;
    
    /**
     * List of jobs / data streams to write.  Java documentation states that
     * Vectors are thread safe
     */
    private Vector<byte[]> mJobList;
    
    /// Release one of these for each job to write out
    private Semaphore mJobCounter;
    
    public FileWriterThread(String filename, String extension)
    {
       mFilenamePrefix = filename;
       mExtension = extension;
       mJobList = new Vector<byte[]>();
       mJobCounter = new Semaphore(0);
    }
    
    /** 
     * Sets a flag that will cause the thread to exit when the next job is
     * complete or the semaphore aquire times out
     */
    public void stopThread()
    {
        mRunFlag = false;
    }
    
    /**
     * Main thread body
     */
    public void run()
    {
       try
       {
           // Thread body
           while(mRunFlag)
           {
               if (mJobCounter.tryAcquire(1, TimeUnit.SECONDS))
               {
                   processSingleJob();
               }
               
               //System.out.println("Waiting for work");
           }
       }
       catch(Exception e)
       {
           System.out.println("Exception while processing a job");
           System.out.println("  " + e.getMessage());
       }
       
       System.out.println("Run flag set to false, exiting gracefully");
       
    }
    
    public void addJob(byte[] data)
    {
        System.out.println("Adding a job with " + data.length + " bytes of data");
        
        mJobList.add(data);
        mJobCounter.release();
    }
    
    private void processSingleJob()
    {
        System.out.println("Processing a job");
        
        String allZeros = "0000";
        String index = String.valueOf(mSuffixNumber);
        
        int suffixLength = index.length();
        if (suffixLength <= allZeros.length())
        {            
            index = allZeros.substring(0, allZeros.length() - suffixLength) + index;
        }
        
        String filename = mFilenamePrefix + index + mExtension;
        try
        {
            FileOutputStream f = new FileOutputStream(filename);
            BufferedOutputStream bos = new BufferedOutputStream(f);
            
            f.write(mJobList.elementAt(0), 0, mJobList.elementAt(0).length);
            f.close();
            
            System.out.println("Wrote " + filename + " (" + mJobList.elementAt(0).length + ")");
        }
        catch(Exception e)
        {
            System.out.println("Error writing the file: " + e.getMessage() );
            e.printStackTrace();
        }
        
        mJobList.remove(0);
        mSuffixNumber++;
    }
    
    public void dataAvailable(byte[] data)
    {
        addJob(data);
    }
}
