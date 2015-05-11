package net.mwales.yawa;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.mwales.yawa.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;



/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment
{

    private final String TAG = "YAWA-ForecastFrag";

    private String webData;

    public ForecastFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> dummyWeatherData = new ArrayList<>();
        dummyWeatherData.add("Today - Sexy - 85 / 90");
        dummyWeatherData.add("Tomorrow - Cold - 1 / 2");
        dummyWeatherData.add("Monday - Work - 65 / 68");
        dummyWeatherData.add("Tuesday - Dumb - 3 / 90");
        dummyWeatherData.add("Tuesday - Dumb - 3 / 90");
        dummyWeatherData.add("Wednesday - Android Shit - 4 / 90");
        dummyWeatherData.add("Friday - Dog Shit - 5 / 90");
        dummyWeatherData.add("Saturday - Scaturday - 6 / 90");
        dummyWeatherData.add("Sunday - Humpiness - 7 / 90");
        dummyWeatherData.add("Tuesday - Dumb - 3 / 90");
        dummyWeatherData.add("Wednesday - Android Shit - 4 / 90");
        dummyWeatherData.add("Friday - Dog Shit - 5 / 90");
        dummyWeatherData.add("Saturday - Scaturday - 6 / 90");
        dummyWeatherData.add("Sunday - Humpiness - 7 / 90");
        dummyWeatherData.add("Wednesday - Android Shit - 4 / 90");
        dummyWeatherData.add("Friday - Dog Shit - 5 / 90");
        dummyWeatherData.add("Saturday - Scaturday - 6 / 90");
        dummyWeatherData.add("Sunday - Humpiness - 7 / 90");

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (lm == null)
        {
            dummyWeatherData.add("Couldn't get location data");
        }
        else
        {
            Location lastKnownLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            dummyWeatherData.add("Lat = " + lastKnownLoc.getLatitude() + ", Long=" + lastKnownLoc.getLongitude() + ", Alt=" + lastKnownLoc.getAltitude() );
        }

        ArrayAdapter<String> weatherAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, dummyWeatherData);

        ListView lv = (ListView) rootView.findViewById(R.id.listview_forecast);

        // http://api.open-notify.org/iss-pass.json?lat=27.78029944&lon=-80.62091783
        FetchWebpage request = new FetchWebpage();
        request.execute("http://api.open-notify.org/iss-pass.json?lat=27.78029944&lon=-80.62091783");

        lv.setAdapter(weatherAdapter);

        return rootView;
    }

    private class FetchWebpage extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... siteUrl)
        {
            String results = "";

            try
            {

                URL website = new URL(siteUrl[0]);
                InputStream dataStream = website.openStream();
                BufferedReader inputData = new BufferedReader( new InputStreamReader(dataStream));

                try
                {
                    String lineOfText = "";
                    while(lineOfText != null)
                    {
                        lineOfText = inputData.readLine();

                        Log.d(TAG, "Data read=" + lineOfText);
                        results = results + lineOfText;
                    }
                }
                catch (IOException eIo)
                {
                    Log.d(TAG, "IO exception.  Probably end of data.  Msg=" + eIo.getMessage());
                }

                Log.d(TAG, "Done reading webpage");

            }
            catch (Exception e)
            {
                Log.e(TAG, "Exception performing background operation: " + e.getMessage());
            }

            return  results;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            ForecastFragment.this.webData = s;
        }
    }
}