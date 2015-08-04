package net.mwales.bookscanner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mwales on 6/14/15.
 */
public class CamPreviewFragment extends Fragment
{
    final static String TAG = CamPreviewFragment.class.getSimpleName();

    static int instanceCounter = 0;

    int instanceId;

    private SurfaceHolder mCamSurface;

    private ICaptureCallback mShutterCallback;

    private TextView mIpInfoView;

    public CamPreviewFragment()
    {
        instanceId = instanceCounter;
        instanceCounter++;

        Log.d(TAG, "Constructor called - Id " + instanceId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView called - Id " + instanceId);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        SurfaceView sv = (SurfaceView) rootView.findViewById(R.id.cam_surface_view);

        if (sv != null)
        {
            Log.d(TAG, "Found the surface view - Id " + instanceId);
            mCamSurface = sv.getHolder();
        }

        Button captureButton = (Button) rootView.findViewById(R.id.capture_page);
        if (captureButton == null)
        {
            Log.wtf(TAG, "Can't find the button in the fragment");
            return rootView;
        }

        captureButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mShutterCallback != null)
                {
                    mShutterCallback.ShutterPressed();
                }
                else
                {
                    Log.d(TAG, "No callback registered for the shutter button");
                }
            }
        });

        sv.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mShutterCallback != null)
                {
                    mShutterCallback.AutoFocus();
                }
                else
                {
                    Log.d(TAG, "No callback registered for the autofocus");
                }
            }
        });

        mIpInfoView = (TextView) rootView.findViewById(R.id.ip_address_view);
        if (mIpInfoView == null)
        {
            Log.e(TAG, "Couldn't find the IP Address View");
        }

        return rootView;
    }

    public SurfaceHolder getSurfaceHolder()
    {
        return mCamSurface;
    }

    public void registerCaptureListener(ICaptureCallback cb)
    {
        mShutterCallback = cb;
    }

    public void setIpInfo(String ipAddress, String portNumber)
    {
        if (mIpInfoView == null)
        {
            Log.d(TAG, "No IP Address view");
            return;
        }

        mIpInfoView.setText(getActivity().getString(R.string.ip_address) + ipAddress +
                            getActivity().getString(R.string.port_number) + portNumber);

    }
}
