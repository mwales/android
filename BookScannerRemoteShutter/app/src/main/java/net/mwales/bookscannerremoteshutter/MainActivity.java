package net.mwales.bookscannerremoteshutter;
import  net.mwales.bookscannerremoteshutter.SettingsActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity
{

    final static String TAG = MainActivity.class.getSimpleName();

    String mBookScannerAddress;

    int mBookScannerPort;

    PlaceholderFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mFragment = new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        loadPreferences();

        updateLabels();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (mFragment.getCaptureButton() == null)
        {
            Log.wtf(TAG, "Start didn't get capture button");
            return;
        }

        mFragment.getCaptureButton().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ShutterTransport st = new ShutterTransport(mBookScannerAddress, mBookScannerPort, "capture");
            }
        });

        if (mFragment.getFocusButton() == null)
        {
            Log.wtf(TAG, "Start didn't get focus button");
            return;
        }

        mFragment.getFocusButton().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ShutterTransport st = new ShutterTransport(mBookScannerAddress, mBookScannerPort, "focus");
            }
        });
    }

    public void loadPreferences()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        mBookScannerAddress = sp.getString("camera_address", "192.168.1.101");
        Log.d(TAG, String.format("Shutter service address: %s", mBookScannerAddress));

        String shutterPort = sp.getString("camera_port_number", "-1");
        if ( (shutterPort != null) && (!shutterPort.isEmpty()))
        {
            try
            {
                mBookScannerPort = Integer.parseInt(shutterPort);
                Log.d(TAG, String.format("Shutter port from preferences: %d", mBookScannerPort));
            }
            catch (NumberFormatException e)
            {
                Log.e(TAG, String.format("Error parsing the port number (%s) from settings: %s", shutterPort, e.getMessage()));
            }
        }
    }

    public void updateLabels()
    {
        Log.d(TAG, String.format("Updating labels with %s and %d", mBookScannerAddress, mBookScannerPort));

        TextView ipLabel = (TextView) findViewById(R.id.ip_label);
        TextView portLabel = (TextView) findViewById(R.id.port_label);

        if ( (ipLabel == null) || (portLabel == null))
        {
            Log.wtf(TAG, "Couldn't find the IP or Port labels");
            return;
        }

        ipLabel.setText(getText(R.string.scanner_ip) + mBookScannerAddress);
        portLabel.setText(String.format("%s%d", getText(R.string.scanner_port), mBookScannerPort));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Intent settingsIntent = new Intent(this, net.mwales.bookscannerremoteshutter.SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getCameraIP()
    {
        return mBookScannerAddress;
    }

    public int getCameraPort()
    {
        return mBookScannerPort;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {

        public PlaceholderFragment()
        {
        }

        Button mCaptureButton;

        Button mFocusButton;

        public Button getCaptureButton()
        {
            return mCaptureButton;
        }

        public Button getFocusButton()
        {
            return mFocusButton;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mCaptureButton = (Button) rootView.findViewById(R.id.capture_button);
            mFocusButton = (Button) rootView.findViewById(R.id.focus_button);

            return rootView;
        }
    }
}
