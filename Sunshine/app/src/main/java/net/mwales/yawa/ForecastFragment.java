package net.mwales.yawa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment
{

    private final String TAG = getClass().getSimpleName();

    ArrayAdapter<String> _weatherAdapter;

    Vector<Date> _passStartTimes;
    Vector<Integer> _passDurations;

    double _latitude;
    double _longitude;

    public ForecastFragment()
    {
        _passStartTimes = new Vector<Date>();
        _passDurations = new Vector<Integer>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        setHasOptionsMenu(true);

        ArrayList<String> dummyWeatherData = new ArrayList<>();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());


        if (sp == null)
        {
            Log.e(TAG, "Error getting the shared preferences");
        }
        else
        {
            Log.d(TAG, "Successfully retrieved shared preferences");

            boolean useGps = sp.getBoolean("pref_use_gps", false);

            String latStr = sp.getString("pref_latitude", "0.0");
            double lat = Double.parseDouble(latStr);

            String lngStr = sp.getString("pref_longitude", "0.0");
            double lng = Double.parseDouble(lngStr);

            String altStr = sp.getString("pref_altitude", "0.0");
            double alt = Double.parseDouble(altStr);

            Log.d(TAG, "preferences:  useGps=" + useGps + ", lat=" + lat + ", long=" + lng + ", alt=" + alt);
        }

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if ( lm == null)
        {
            dummyWeatherData.add("Couldn't get location services");
        }
        else
        {
            Location lastKnownLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLoc == null)
            {
                dummyWeatherData.add("Couldn't get last known location");
            }
            else
            {
                dummyWeatherData.add("Lat = " + lastKnownLoc.getLatitude() + ", Long=" + lastKnownLoc.getLongitude() + ", Alt=" + lastKnownLoc.getAltitude());
            }
        }

        _weatherAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, dummyWeatherData);



        ListView lv = (ListView) rootView.findViewById(R.id.listview_forecast);

        lv.setAdapter(_weatherAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Create an intent to launch details activitiy when item clicked
                Intent detailsIntent = new Intent();
                detailsIntent.setClass(getActivity(), DetailsActivity.class);
                detailsIntent.putExtra("net.mwales.yawa.latitude", _latitude);
                detailsIntent.putExtra("net.mwales.yawa.longitude", _longitude);
                detailsIntent.putExtra("net.mwales.yawa.passStart", _passStartTimes.get(position).getTime());
                detailsIntent.putExtra("net.mwales.yawa.passDuration", _passDurations.get(position).intValue());

                startActivity(detailsIntent);
            }
        });

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
        Log.d(TAG, "options menu item selected = " + item.getItemId());

        switch (item.getItemId())
        {
            case R.id.action_refresh:

                Log.d(TAG, "Refresh menu action started");

                FetchWebpage downloaderTask = new FetchWebpage();
                //downloaderTask.execute(getUrl(27.78029944,-80.62091783, 0));
                downloaderTask.execute(getUrlViaLocation());
                return true;

            case R.id.action_settings:

                Log.d(TAG, "Settings menu selected");
                Intent launchSettings = new Intent();
                launchSettings.setClass(getActivity(), SettingsActivity.class);
                startActivity(launchSettings);
                return true;

            default:
                Log.d(TAG, "Unknown menu item selected");
                return super.onOptionsItemSelected(item);
        }


    }


    private String getUrlViaLocation()
    {
        final double KSC_LAT = 28.608082;
        final double KSC_LONG = -80.604089;
        final double KSC_ALT = 1;

        boolean useGps = false;
        double lat = KSC_LAT;
        double lng = KSC_LONG;
        double alt = KSC_ALT;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (sp == null)
        {
            Log.e(TAG, "Error getting the shared preferences");
        }
        else
        {
            Log.d(TAG, "Successfully retrieved shared preferences");

            useGps = sp.getBoolean("pref_use_gps", false);

            String latStr = sp.getString("pref_latitude", "0.0");
            lat = Double.parseDouble(latStr);

            String lngStr = sp.getString("pref_longitude", "0.0");
            lng = Double.parseDouble(lngStr);

            String altStr = sp.getString("pref_altitude", "0.0");
            alt = Double.parseDouble(altStr);

            Log.d(TAG, "preferences:  useGps=" + useGps + ", lat=" + lat + ", long=" + lng + ", alt=" + alt);
        }
        if (useGps)
        {
            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (lm == null)
            {
                Log.e(TAG, "Failed to get good location manager reference");
            }
            else
            {
                Location lastKnownLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lat = lastKnownLoc.getLatitude();
                lng = lastKnownLoc.getLongitude();
                alt = lastKnownLoc.getAltitude();

                Log.d(TAG, "GPS Location Info: lat=" + lat + ", long=" + lng + ", alt=" + alt);
            }
        }

        return getUrl(lat, lng, alt);
    }

    private String getUrl(double latitude, double longitude, double altitude)
    {
        // API doesn't like altitude less than 0, but that is valid in real life (death valley duh)
        if (altitude <= 0)
            altitude = 0.1;

        _latitude = latitude;
        _longitude = longitude;

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
            ForecastFragment.this._passDurations.clear();
            ForecastFragment.this._passStartTimes.clear();

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
                    ForecastFragment.this._passStartTimes.add(risetime);
                    ForecastFragment.this._passDurations.add(new Integer(passDuration));

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