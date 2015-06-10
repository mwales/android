package net.mwales.yawa;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;
import android.app.Fragment;



import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsFragment extends PreferenceFragment
{
    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setupActionBar();

        addPreferencesFromResource(R.xml.pref_general);

        final Preference latPref = findPreference(getString(R.id.pref_latitude));
        if (latPref == null)
        {
            Log.e(TAG, "couldn't find lat pref id");
        }
        else
        {
            latPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
            {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue)
                {
                    latPref.setSummary(newValue.toString());
                    return true;
                }
            });
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            // Show the Up button in the action bar.
            ActionBar ab = getActivity().getActionBar();

            if (ab == null)
            {
                Log.d(TAG, "ActionBar is null");
            }
            else
            {
                Log.d(TAG, "ActionBar not null");
                ab.setDisplayHomeAsUpEnabled(true);
            }

        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        /*if (id == android.R.id.home)
        {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }*/

        switch(id)
        {
            case R.id.action_settings:
                Log.d(TAG, "Going to start settings activity");
                Intent startSettingsActivity = new Intent(getActivity(), SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener()
    {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value)
        {
            String stringValue = value.toString();

            if (preference instanceof ListPreference)
            {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            }
            else if (preference instanceof RingtonePreference)
            {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue))
                {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                }
                else
                {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null)
                    {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    }
                    else
                    {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            }
            else
            {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    }; */


}
