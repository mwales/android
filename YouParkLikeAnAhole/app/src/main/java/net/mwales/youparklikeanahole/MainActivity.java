package net.mwales.youparklikeanahole;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import net.mwales.youparklikeanahole.FragmentHostActivity;

//import android.support.v4.app.Fragment;



public class MainActivity extends FragmentHostActivity implements DetailsFragment.OnFragmentInteractionListener
{
    private final String TAG = "NPJ";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected Fragment createFragment()
    {
        return new DetailsFragment();
    }



    @Override
    public void onFragmentInteraction(Uri uri)
    {
        Log.d(TAG, "onFragmentInteraction listener called with URI" + uri.toString());
    }


}
