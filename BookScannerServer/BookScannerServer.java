import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.lang.Throwable;
import java.io.IOException;

public class BookScannerServer
{
    private ServerSocket mServer;
    
    private DataSink mDataWriter;
    
    public BookScannerServer(int port)
    {
        try
        {
           mServer = new ServerSocket(port);
           mServer.setReuseAddress(true);
        }
        catch (Exception e)
        {
            System.out.println("Couldn't open server on port " + port);
        }

    }
    
    public void registerDataSink(DataSink ds)
    {
        mDataWriter = ds;
    }

    public void handleClients()
    {
        try
        {
            Socket client = mServer.accept();

            DataInputStream dis = new DataInputStream(client.getInputStream());

            readStream(dis);
            
            System.out.println("Done receiving data");
            dis.close();
            client.close();
        }
        catch (Exception e)
        {
            System.out.println("Exception: " + e.getMessage() );
            e.printStackTrace();
        }
    }
    
    private void readStream(DataInputStream dis) throws IOException
    {
        int numBytesToRead = dis.readInt();
        
        System.out.println("Next picture size=" + numBytesToRead);
        
        // Client will return 0 for size if no more pictures are going to be sent
        if (numBytesToRead == 0)
        {
            // Done receiving
            return;
        }

        // Prepare to read the picture data
        int offset = 0;
        byte[] pictureData = new byte[numBytesToRead];
        
        while (numBytesToRead > 0)
        {
            
            int bytesRead = dis.read(pictureData, offset, numBytesToRead);
            if (bytesRead == -1)
            {
                System.out.println("Error reading the picture data");
                return;
            }

            offset += bytesRead;
            numBytesToRead -= bytesRead;
            //System.out.println("Picture data bytes received=" + bytesRead + ", and bytes left=" + numBytesToRead);
            
        }
        
        mDataWriter.dataAvailable(pictureData);
        
    }
}






