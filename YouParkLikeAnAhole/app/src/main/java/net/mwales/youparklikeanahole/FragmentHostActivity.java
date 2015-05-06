package net.mwales.youparklikeanahole;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;


public abstract class FragmentHostActivity extends Activity
{
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_host);
        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                    .add(R.id.generic_fragment_container, createFragment() )
                    .commit();
        }
    }

}
