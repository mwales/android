package net.mwales.yawa;

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
import android.widget.TextView;

import java.util.Date;


public class DetailsActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {

        public PlaceholderFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_details, container, false);

            TextView displayText = (TextView) rootView.findViewById(R.id.details_text);

            double locationLat = getActivity().getIntent().getDoubleExtra("net.mwales.yawa.latitude", 0.0);
            double locationLong = getActivity().getIntent().getDoubleExtra("net.mwales.yawa.longitude", 0.0);
            Date passStartTime = new Date();
            passStartTime.setTime(getActivity().getIntent().getLongExtra("net.mwales.yawa.passStart", 0));
            int passDuration = getActivity().getIntent().getIntExtra("net.mwales.yawa.passDuration", 1);

            displayText.append("\n\nLat = " + Double.toString(locationLat));
            displayText.append("\nLong = " + Double.toString(locationLong));
            displayText.append("\n\nStart Time = " + passStartTime.toString());
            displayText.append("\nDuration = " + Integer.toString(passDuration));


            Log.d("YAWA-ForecastFrag", "Lat retrieved from intent = " + locationLat);



            return rootView;
        }
    }
}
