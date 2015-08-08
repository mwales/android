package net.mwales.bookscanner;

import android.os.HandlerThread;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Allows shutter clients to connect to the application and send the application focus or capture
 * commands
 */
public class ShutterServer implements Runnable
{
    final static String TAG = ShutterServer.class.getSimpleName();

    ICaptureCallback mCamera;

    /// Set to false to have the server thread exit
    boolean mServerRun;

    int mPortNumber;

    Thread mServerThread;

    public ShutterServer(ICaptureCallback cameraDevice, int portNumber)
    {
        mCamera = cameraDevice;
        mPortNumber = portNumber;
        mServerRun = true;

        mServerThread = new Thread(this, "ShutterServer");
        mServerThread.start();
    }

    public void run()
    {
        Log.d(TAG, String.format("Staring the shutter server on port %d", mPortNumber));

        try
        {
            ServerSocket ss = new ServerSocket(mPortNumber);

            /// Have timeout every 100ms so we don't block for a long time during accept (we
            /// wouldn't be able to shutdown the application
            ss.setSoTimeout(100);

            while (ServerKeepRunning())
            {
                try
                {
                    Socket shutterClient = ss.accept();

                    Log.d(TAG, "New client accepted");
                    HandleShutterClient(shutterClient);
                }
                catch(InterruptedIOException e)
                {
                    //Log.d(TAG, "Interrupt exception, do nothing!");
                    continue;
                }
                catch (IOException e)
                {
                    Log.e(TAG, String.format("Exception occurred during accept: %s", e.getMessage()));
                    break;
                }


            }

            ss.close();
        }
        catch(IOException e)
        {
            Log.e(TAG, String.format("Error in socket code: %s", e.getMessage()));
        }

        Log.d(TAG, "Exitting server thread");
    }

    public void StopServer()
    {
        Log.d(TAG, "Stopping the shutter server");
        ServerStopRunning();

        try
        {
            mServerThread.join();
            Log.d(TAG, "Server stopped");
        }
        catch(Exception e)
        {
            Log.e(TAG, String.format("Error occurred while waiting for server socket to stop: ", e.getMessage()));
        }

    }

    private synchronized boolean ServerKeepRunning()
    {
        return mServerRun;
    }

    private synchronized void ServerStopRunning()
    {
        mServerRun = false;
    }

    /// Client has 1 second to send a command before the connection is dropped
    private void HandleShutterClient(Socket client)
    {
        try
        {
            BufferedInputStream reader = new BufferedInputStream(client.getInputStream());

            int numSleepLoops = 10;
            while(numSleepLoops-- > 0)
            {
                int bytesAvailable = reader.available();
                if (bytesAvailable == 0)
                {
                    mServerThread.sleep(100);
                }
                else
                {
                    byte[] buffer = new byte[bytesAvailable];
                    int bytesRead = reader.read(buffer, 0, bytesAvailable);

                    if (bytesRead != bytesAvailable)
                    {
                        Log.w(TAG, String.format("Bytes available = %d and bytes read = %d", bytesAvailable, bytesRead));
                    }

                    String command = new String(buffer);

                    Log.d(TAG, String.format("Client command: %s", command));

                    if (command.trim().equals("capture"))
                    {
                        Log.d(TAG, "Client initiated capture");
                        mCamera.ShutterPressed();
                    }
                    else if (command.trim().equals("focus"))
                    {
                        Log.d(TAG, "Client initiated auto focus");
                        mCamera.AutoFocus();
                    }
                    else
                    {
                        Log.e(TAG, "Client command unknown");
                    }
                }
            }

                reader.close();
                client.close();

        }
        catch(Exception e)
        {
            Log.e(TAG, String.format("Exception while handline shutter client: %s", e.getMessage()));
        }
    }


}
