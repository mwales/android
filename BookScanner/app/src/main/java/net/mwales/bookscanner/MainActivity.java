package net.mwales.bookscanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.preference.Preference;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;
import java.util.List;


public class MainActivity extends ActionBarActivity implements SurfaceHolder.Callback, ICaptureCallback, Camera.AutoFocusCallback, Camera.ShutterCallback, Camera.PictureCallback
{
    final static String TAG = MainActivity.class.getSimpleName();

    Camera mCamera;
    SurfaceHolder mCameraPreviewSH;
    CamPreviewFragment mFragment;
    int mBackfacingCameraId = -1;

    int mShutterPort;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate called");

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
        {
            Log.d(TAG, "Creating a new fragment");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            mFragment = new CamPreviewFragment();
            ft.add(R.id.container, mFragment).commit();
        }
        else
        {
            Log.d(TAG, "Finding existing fragment");
            mFragment = (CamPreviewFragment) getSupportFragmentManager().findFragmentById(R.id.container);

            if (mFragment == null)
            {
                Log.wtf(TAG, "Couldn't find the existing fragment");
            }

            //mFragment = getSupportFragmentManager().findFragmentById(R.id.)
        }

        Log.d(TAG, "Number of cameras =" + Camera.getNumberOfCameras());

        for(int i = 0; i < Camera.getNumberOfCameras(); i++)
        {
            Camera.CameraInfo ci = new Camera.CameraInfo();
            Camera.getCameraInfo(i, ci);
            Log.d(TAG, "Camera " + i + " can disable sound " + ci.canDisableShutterSound);
            Log.d(TAG, "Camera " + i + " is facing " + (ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK ? "Back" : "Front"));
            Log.d(TAG, "Camera " + i + " orientation in deg " + ci.orientation);

            if (ci.facing == CameraInfo.CAMERA_FACING_BACK)
            {
                // This is the camera we want to use!
                mBackfacingCameraId = i;
                Log.d(TAG, "Going to use this camera for application: " + mBackfacingCameraId);
            }
        }

       mFragment.registerCaptureListener(this);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Log.d(TAG, "onStart called");

        mCamera = Camera.open(mBackfacingCameraId);

        Camera.Parameters camParams = mCamera.getParameters();

        List<Camera.Size> sizeList = camParams.getSupportedPictureSizes();
        int desiredWidth = 0;
        int desiredHeight = 0;
        double desiredMp = 0.0;
        for(int i = 0; i < sizeList.size(); i++)
        {
            long width = sizeList.get(i).width;
            long height = sizeList.get(i).height;
            double mp = width * height / 1000.0 / 1000.0;
            DecimalFormat doubleFormatter = new DecimalFormat("0.#");
            Log.d(TAG, "Camera supports " + width + "x" + height + " (" + doubleFormatter.format(mp) + " MP)");

            // Save the highest resolution resolution parameters
            if (mp > desiredMp)
            {
                desiredWidth = (int) width;
                desiredHeight = (int) height;
                desiredMp = mp;
            }

        }

        // Set the resolution to the highest mode
        camParams.setPictureSize(desiredWidth, desiredHeight);
        mCamera.setParameters(camParams);

        Log.d(TAG, "Set picture resolution");

        mCameraPreviewSH = mFragment.getSurfaceHolder();
        mCameraPreviewSH.addCallback(this);

        // Setup camera orientation
        setCameraDisplayOrientation();

        displayNetworkInformation();
    }





    @Override
    protected void onResume()
    {
        super.onResume();

        Log.d(TAG, "onResume called - starting preview");

        mCamera.startPreview();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        Log.d(TAG, "onPause called - stopping preview");

        mCamera.stopPreview();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        Log.d(TAG, "onStop called - releasing camera");

        mCamera.release();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.d(TAG, "surfaceCreated callback executed");

        try
        {
            mCamera.setPreviewDisplay(mCameraPreviewSH);
            Log.d(TAG, "Set preview display successfully");
        }
        catch(Exception e)
        {
            Log.d(TAG, "Exception while trying to set the camera preview display");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        Log.d(TAG, "surfaceChanged callback executed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.d(TAG, "surfaceDestroyed callback executed");
    }


    @Override
    public void ShutterPressed()
    {
        Log.d(TAG, "Shutter pressed");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean alwaysAutofocus = sp.getBoolean("always_autofocus", false);

        Log.d(TAG, "Always Autofocus Setting=" + alwaysAutofocus);

        if (alwaysAutofocus)
        {
            AutoFocus();
        }

        try
        {
            mCamera.takePicture(this, null, this);
        }
        catch(Exception e)
        {
            Log.d(TAG, "Exception during picture: " + e.getMessage());
            Toast toast = Toast.makeText(this, "Error taking picture: " + e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void AutoFocus()
    {
        Log.d(TAG, "Autofocus pressed");
        mCamera.autoFocus(this);
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera)
    {
        Log.d(TAG, "AutoFocus complete, success =" + success);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera)
    {
        Log.d(TAG, "Data = " + data.length + " bytes long");

        // Ready the camera to take another picture
        mCamera.startPreview();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String serverAddress = sp.getString("server_address", "192.168.1.100");

        // Parse the port number from the preferences string
        String serverPortStr = sp.getString("server_port_number", "5975");
        int serverPort = 5975;
        try
        {
            serverPort = Integer.parseInt(serverPortStr);
        }
        catch(NumberFormatException e)
        {
            Log.e(TAG, "Error parsing the server port: " + serverPortStr);
        }

        Log.d(TAG, "Settings retrieved: ServerAddr=" + serverAddress + " and ServerPort=" + serverPort);

        PhotoTransport pt = new PhotoTransport(serverAddress, serverPort, data);
    }

    @Override
    public void onShutter()
    {
        Log.d(TAG, "Image capture started");
    }

    /**
     * This method shamelessly stolen from Android documentation
     */
    public void setCameraDisplayOrientation()
    {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(mBackfacingCameraId, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation)
        {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else
        {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
     }

    protected void displayNetworkInformation()
    {
        Log.d(TAG, "DisplayNetworkInformation started");

        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wm == null)
        {
            Log.e(TAG, "Couldn't access the Wifi Manager");
            return;
        }

        WifiInfo wi = wm.getConnectionInfo();

        if (wi.getSupplicantState() != SupplicantState.COMPLETED)
        {
            // Wifi not up
            Log.d(TAG, "Wifi not connected");

        }
        else
        {
            Log.d(TAG, "IP Address =" + wi.getIpAddress());

            int ipAddress = wi.getIpAddress();
            String quadDot = Integer.toString(0xff & ipAddress) + ".";

            ipAddress = ipAddress >> 8;
            quadDot = quadDot + Integer.toString(0xff & ipAddress) + ".";

            ipAddress = ipAddress >> 8;
            quadDot = quadDot + Integer.toString(0xff & ipAddress) + ".";

            ipAddress = ipAddress >> 8;
            quadDot = quadDot + Integer.toString(0xff & ipAddress);

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            String shutterPort = sp.getString("shutter_port_number", "-1");

            mFragment.setIpInfo(quadDot, shutterPort);
        }
        Log.d(TAG, "Info=" + wi.toString());
    }
}
