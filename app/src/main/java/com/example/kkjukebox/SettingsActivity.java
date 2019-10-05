package com.example.kkjukebox;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        private ListPreference weather, game, kk;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
            weather = findPreference("weather_prefs");
            game = findPreference("game_prefs");
            kk = findPreference("kk_prefs");

            if (weather != null && game != null && kk != null)  {
                weather.setOnPreferenceChangeListener(this);
                game.setOnPreferenceChangeListener(this);
                kk.setOnPreferenceChangeListener(this);
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            boolean val = false;
            String s = newValue.toString();
            if(preference == weather) {
                weather.setValue(s);
                val = true;
            }
            else if(preference == game) {
                game.setValue(s);
                val = true;
            }
            else if(preference == kk) {
                kk.setValue(s);
                val = true;
            }

            return val;
        }
    }
}