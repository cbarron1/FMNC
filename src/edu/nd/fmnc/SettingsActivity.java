package edu.nd.fmnc;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();

		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(this);
	}

	public static class SettingsFragment extends PreferenceFragment {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
			
		}

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
		if (key.equals("prefEnableWifi")) {
			FMNCActivity.setWifiOn(pref.getBoolean(key, true));
		} else if (key.equals("pref_enableCell")) {
			FMNCActivity.setCellOn(pref.getBoolean(key, false));
		} else if (key.equals("pref_wifi_roam")) {
			FMNCActivity.setWifiRoam(pref.getBoolean(key, true));
		} else if (key.equals("pref_cell_roam")) {
			FMNCActivity.setCellRoam(pref.getBoolean(key, false));
		}
	}



}
