package net.mwales.youparklikeanahole;

import java.util.Date;

/**
 * Created by mwales on 4/24/15.
 */
public class ParkingJobModel
{
    public ParkingJobModel()
    {
        mId = -1;
        mCulprit = "";
        mDescription = "";
        mDate = null;
    }


    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        mDescription = description;
    }

    public int getId()
    {
        return mId;
    }

    public void setId(int id)
    {
        mId = id;
    }

    public String getCulprit()
    {
        return mCulprit;
    }

    public void setCulprit(String culprit)
    {
        mCulprit = culprit;
    }

    public Date getDate()
    {
        return mDate;
    }

    public void setDate(Date date)
    {
        mDate = date;
    }

    private String mCulprit;

    private String mDescription;

    private int mId;

    private Date mDate;
}
