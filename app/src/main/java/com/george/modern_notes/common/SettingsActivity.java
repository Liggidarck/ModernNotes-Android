package com.george.modern_notes.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.george.modern_notes.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String theme_app = sharedPreferences.getString(getString(R.string.root_theme_app), getString(R.string.root_theme_violet));

        assert theme_app != null;
        if(theme_app.equals(getString(R.string.root_theme_orange)))
            setTheme(R.style.Orange);

        if(theme_app.equals(getString(R.string.root_theme_blue)))
            setTheme(R.style.BlueTheme);

        if(theme_app.equals(getString(R.string.root_theme_aqua_blue)))
            setTheme(R.style.AquaBlueTheme);

        if(theme_app.equals(getString(R.string.root_theme_green)))
            setTheme(R.style.GreenTheme);

        if(theme_app.equals(getString(R.string.root_theme_red)))
            setTheme(R.style.RedTheme);

        if(theme_app.equals(getString(R.string.root_theme_violet)))
            setTheme(R.style.VioletTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            final Preference pref_theme = findPreference(getString(R.string.root_theme_app));
            assert pref_theme != null;
            pref_theme.setOnPreferenceChangeListener((preference, newValue) -> {
                Objects.requireNonNull(getActivity()).recreate();
                return true;
            });

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}