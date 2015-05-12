package net.mwales.yawa;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.mwales.yawa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment
{

    private final String TAG = "YAWA-ForecastFrag";

    ArrayAdapter<String> _weatherAdapter;

    public ForecastFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        setHasOptionsMenu(true);

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

        _weatherAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, dummyWeatherData);

        ListView lv = (ListView) rootView.findViewById(R.id.listview_forecast);

        lv.setAdapter(_weatherAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.forecast_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_refresh)
        {
            Log.d(TAG, "Refresh menu action started");

            FetchWebpage downloaderTask = new FetchWebpage();
            downloaderTask.execute(getUrl(27.78029944,-80.62091783, 0));
            return true;
        }
        else
        {
            Log.d(TAG, "Unknown menu item selected");
            return super.onOptionsItemSelected(item);
        }


    }

    private String getUrlViaGps()
    {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (lm == null)
        {
            final double KSC_LAT = 28.608082;
            final double KSC_LONG = -80.604089;
            final double KSC_ALT = 1;
            return getUrl(KSC_LAT, KSC_LONG, KSC_ALT);
        }
        else
        {
            Location lastKnownLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return getUrl(lastKnownLoc.getLatitude(), lastKnownLoc.getLongitude(), lastKnownLoc.getAltitude() );
        }
    }

    private String getUrl(double latitude, double longitude, double altitude)
    {
        // API doesn't like altitude less than 0, but that is valid in real life (death valley duh)
        if (altitude <= 0)
            altitude = 0.1;

        Uri.Builder retVal = new Uri.Builder();
        retVal.scheme("http")
                .authority("api.open-notify.org")
                .appendPath("iss-pass.json")
                .appendQueryParameter("lat", Double.toString(latitude))
                .appendQueryParameter("lon", Double.toString(longitude))
                .appendQueryParameter("alt", Double.toString(altitude))
                .appendQueryParameter("n", "10")
                .build();

        Log.d(TAG, "URI Built=" + retVal.toString());
        // http://api.open-notify.org/iss-pass.json?lat=27.78029944&lon=-80.62091783

        return retVal.toString();

        //return "http://mwales.net";

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
        /**
         * The format of the response looks like the following:
         * 
         * {
         *   "message": "success", 
         *   "request": {
         *     "altitude": 0.1, 
         *     "datetime": 1431396628, 
         *     "latitude": 27.78029944, 
         *     "longitude": -80.62091783, 
         *     "passes": 5
         *   }, 
         *   "response": [
         *     {
         *       "duration": 592, 
         *       "risetime": 1431419234
         *     }, 
         *     {
         *       "duration": 570, 
         *       "risetime": 1431425021
         *     }, 
         *     {
         *       "duration": 557, 
         *       "risetime": 1431448573
         *     }, 
         *     {
         *       "duration": 607, 
         *       "risetime": 1431454330
         *     }
         *   ]
         * }
         *
         */
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            //ForecastFragment.this.webData = s;

            Log.d(TAG, "PostExecute called with string length: " + s.length());

            ForecastFragment.this._weatherAdapter.clear();

            try
            {
                JSONObject responseObj = (JSONObject) new JSONTokener(s).nextValue();

                Iterator<String> keyList = responseObj.keys();
                while(keyList.hasNext())
                {
                    Log.d(TAG, "Key=" + keyList.next());
                }
                JSONArray passList = responseObj.getJSONArray("response");

                Log.d(TAG, "Responses size = " + passList.length());

                for(int i = 0; i < passList.length(); i++)
                {
                    JSONObject ro = passList.getJSONObject(i);

                    int passDuration = ro.getInt("duration");

                    final long MS_PER_SEC = 1000;
                    Date risetime = new Date(ro.getLong("risetime") * MS_PER_SEC);

                    Log.d(TAG, "Pass Start Time = " + risetime.toString() + ", duration=" + passDuration);

                    ForecastFragment.this._weatherAdapter.add("Pass Start Time = " + risetime.toString() + "\n   Duration=" + passDuration);


                }
            }
            catch(JSONException je)
            {
                Log.d(TAG, "Exception parsing JSON response: " + je.getMessage());
            }


        }
    }
}