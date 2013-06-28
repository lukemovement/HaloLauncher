package com.paranoid.lukemovement.halolauncher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Created by lukepring on 25/06/13.
 */

public class Settings extends PreferenceActivity {

    SharedPreferences SP;
    CheckBoxPreference CloseAfterNewApp;
    CheckBoxPreference ShowFavourites;
    CheckBoxPreference ShowToggles;
    CheckBoxPreference ShowAllApps;

    public void onBackPressed() {
            startActivity(new Intent(this, MainActivity.class).addFlags(0x00002000
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        SP = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        CloseAfterNewApp = (CheckBoxPreference) findPreference("CloseAfterNewApp");
        CloseAfterNewApp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SP.edit().putBoolean("CloseAfterNewApp", (Boolean) newValue).commit();
                return true;
            }
        });

        ShowFavourites = (CheckBoxPreference) findPreference("ShowFavourites");
        ShowFavourites.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SP.edit().putBoolean("ShowFavourites", (Boolean) newValue).commit();
                return true;
            }
        });

        ShowToggles = (CheckBoxPreference) findPreference("ShowToggles");
        ShowToggles.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SP.edit().putBoolean("ShowToggles", (Boolean) newValue).commit();
                return true;
            }
        });

        ShowAllApps = (CheckBoxPreference) findPreference("ShowAllApps");
        ShowAllApps.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SP.edit().putBoolean("ShowAllApps", (Boolean) newValue).commit();
                return true;
            }
        });

        getSettings();

    }

    private void getSettings() {
        CloseAfterNewApp.setChecked(SP.getBoolean("CloseAfterNewApp", true));
        ShowFavourites.setChecked(SP.getBoolean("ShowFavourites", true));
        ShowToggles.setChecked(SP.getBoolean("ShowToggles", true));
        ShowAllApps.setChecked(SP.getBoolean("ShowAllApps", true));
    }
}
