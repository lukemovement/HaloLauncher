/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paranoid.lukemovement.halolauncher;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
    static SharedPreferences SP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("This app will not work on your device because your rom does not support HALO\n\nPlease install a rom that does support this feature(e.g. 'Paranoid Android')");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                } });
        }

        SP = getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:     		finish();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class).addFlags(0x00002000
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if((!SP.getBoolean("ShowFavourites", true) && !SP.getBoolean("ShowToggles", true)) && !SP.getBoolean("ShowAllApps", true)) {
                i = 3;
            }
            switch (i) {
                case 0:
                    if(SP.getBoolean("ShowFavourites", true)) {
                        return new FavouritesList();
                    } else if (SP.getBoolean("ShowAllApps", true)) {
                        return new AppList();
                    } else {
                        return new Toggles();
                    }
                case 1:
                    if (SP.getBoolean("ShowFavourites", true) && SP.getBoolean("ShowAllApps", true)) {
                        return new AppList();
                    } else {
                        return new Toggles();
                    }
                case 2:
                    return new Toggles();
                default:
                    return new Nothing();
            }

        }

        @Override
        public int getCount() {
            if((!SP.getBoolean("ShowFavourites", true) && SP.getBoolean("ShowToggles", true)) && SP.getBoolean("ShowAllApps", true) ||
                    (SP.getBoolean("ShowFavourites", true) && !SP.getBoolean("ShowToggles", true)) && SP.getBoolean("ShowAllApps", true)  ||
                    (SP.getBoolean("ShowFavourites", true) && SP.getBoolean("ShowToggles", true)) && !SP.getBoolean("ShowAllApps", true) ) {
                return 2;
            } else if(SP.getBoolean("ShowFavourites", true) && SP.getBoolean("ShowToggles", true) && SP.getBoolean("ShowAllApps", true)) {
                return 3;
            } else {
                return 1;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if((!SP.getBoolean("ShowFavourites", true) && !SP.getBoolean("ShowToggles", true)) && !SP.getBoolean("ShowAllApps", true)) {
                position = 3;
            }
            switch (position) {
                case 0:
                    if(SP.getBoolean("ShowFavourites", true)) {
                        return "Favourites";
                    } else if (SP.getBoolean("ShowAllApps", true)) {
                        return "All Apps";
                    } else {
                        return "Toggles";
                    }
                case 1:
                    if (SP.getBoolean("ShowFavourites", true) && SP.getBoolean("ShowAllApps", true)) {
                        return "All Apps";
                    } else {
                        return "Toggles";
                    }
                case 2:
                    return "Toggles";
                default:
                    return "";
            }
        }

    }

}
