package net.mwales.youparklikeanahole;

import android.app.Application;
import android.content.Context;

import java.util.Vector;

/**
 * Created by mwales on 4/27/15.
 */
public class PjMgr
{
    // Instance pointer for singleton
    private static PjMgr mInstance;

    // Not really sure why I need to save this context variable?
    private Context mContext;

    private Vector<ParkingJobModel> mJobs;

    private PjMgr(Context appContext)
    {
        mContext = appContext;

        mJobs = new Vector<ParkingJobModel>();
    }

    public static PjMgr getInstance(Context c)
    {
        if (mInstance == null)
        {
            mInstance = new PjMgr(c.getApplicationContext());
        }

        return mInstance;
    }

    public ParkingJobModel getJob(int id)
    {
        for(ParkingJobModel pjm : mJobs)
        {
            if (pjm.getId() == id)
            {
                return pjm;
            }
        }
    }
}
