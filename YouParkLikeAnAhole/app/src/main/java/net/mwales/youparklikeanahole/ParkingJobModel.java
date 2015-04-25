package net.mwales.youparklikeanahole;

/**
 * Created by mwales on 4/24/15.
 */
public class ParkingJobModel
{

    private int mId;

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

    private String mDescription;
}
