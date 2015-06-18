

package net.mwales.bookscanner;


import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by mwales on 6/16/15.
 */
public class PhotoTransport
{
    final static String TAG = PhotoTransport.class.getSimpleName();

    String mHostname;

    int mPortNumber;

    byte[] mData;

    public PhotoTransport(String host, int port, byte[] data)
    {
        mHostname = host;
        mPortNumber = port;
        mData = data;

        TransportTask worker = new TransportTask();
        worker.execute();
    }

    class TransportTask extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            Log.d(TAG, "Doing stuff in the background");

            try
            {
                Log.d(TAG, "Creating socket");
                Socket server = new Socket(mHostname, mPortNumber);

                Log.d(TAG, "Creating output stream");
                DataOutputStream dataStream = new DataOutputStream(server.getOutputStream());

                Log.d(TAG, "Writing the size of data: " + mData.length);
                dataStream.writeInt(mData.length);

                Log.d(TAG, "Writing output data, len = " + mData.length);
                dataStream.write(mData, 0, mData.length);

                Log.d(TAG, "Closing stream");
                dataStream.close();

                Log.d(TAG, "Closing socket");
                server.close();

                return "Asynch task success";
            }
            catch (Exception e)
            {
                Log.e(TAG, "Exception: " + e.getMessage());
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            Log.d(TAG, "onPostExecute = " + result);
        }
    }
}
