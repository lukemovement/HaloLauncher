package com.paranoid.lukemovement.halolauncher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lukepring on 24/06/13.
 */
public class FavouritesList extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    EditText input;
    //private ProgressDialog pd;
    String Complete = "false";
    private ListView mAppsList;
    private AppListAdapter mAdapter;
    private List<App> mApps;
    SharedPreferences SP;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.applist, container, false);
        mAppsList = (ListView) rootView.findViewById(R.id.appslist);
        SP = getActivity().getSharedPreferences(getActivity().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SP.registerOnSharedPreferenceChangeListener(SPListener);
        new LoadGUI().execute();
        return rootView;

    }

    SharedPreferences.OnSharedPreferenceChangeListener SPListener = new SharedPreferences.OnSharedPreferenceChangeListener(){
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            new LoadGUI().execute();
        }
    };

    private List<App> loadInstalledApps()  {

        List<App> apps = new ArrayList<App>();
        PackageManager packageManager = getActivity().getPackageManager();

        List<PackageInfo> packs = packageManager.getInstalledPackages(0);

        String list = SP.getString("FavouriteApps", "");

        for(int i=0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (p.versionName == null || !list.contains(" " + p.packageName + " ")) {
                continue;
            }
            App app = new App();
            app.setTitle(p.applicationInfo.loadLabel(packageManager).toString());
            app.setPackageName(p.packageName);
            if (!p.packageName.equals(getActivity().getPackageName())) {
                if(getActivity().getPackageManager().getLaunchIntentForPackage(app.getPackageName()) != null) {
                    apps.add(app);
                }
            }

        }
        Collections.sort(apps, new AppSorter());
        return apps;
    }


    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, int position, long id) {
        final App app = (App) parent.getItemAtPosition(position);
        final String PackageName = app.getPackageName();
        final String AppName = app.getTitle();
        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(PackageName);
        intent.addFlags(0x00002000
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if(SP.getBoolean("CloseAfterNewApp", true)) {
            getActivity().finish();
        }
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, final View view, int position, long id) {
        final App app = (App) parent.getItemAtPosition(position);
        final String PackageName = app.getPackageName();
        final String AppName = app.getTitle();

        final String list = SP.getString("FavouriteApps", "");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(!list.contains(" " + PackageName + " ")) {

            builder.setTitle("Favourite - " + AppName);
            builder.setMessage("Add to favourites?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    SP.edit().putString("FavouriteApps", list + " " + PackageName + " ").commit();
                    dialog.dismiss();
                }

            });

            builder.setNegativeButton("NO", null);

        } else {

            builder.setTitle("Favourite - " + AppName);
            builder.setMessage("Remove from favourites?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    SP.edit().putString("FavouriteApps", list.replace(" " + PackageName + " ", "")).commit();
                    dialog.dismiss();
                }

            });

            builder.setNegativeButton("NO", null);

        }

        AlertDialog alert = builder.create();
        alert.show();

        return false;
    }

    private class LoadIconsTask extends AsyncTask<App, Void, Void> {
        @Override
        protected Void doInBackground(App... apps) {
            try {
            Map<String, Drawable> icons = new HashMap<String, Drawable>();
            PackageManager manager = getActivity().getPackageManager();

            for (App app : apps) {
                    String pkgName = app.getPackageName();
                    Drawable ico = null;
                    try {
                        Intent i = manager.getLaunchIntentForPackage(pkgName);

                        if (i != null) {
                            ico = manager.getActivityIcon(i);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.e("ERROR", "Unable to find icon for package '" + pkgName + "': " + e.getMessage());
                    }
                    icons.put(app.getPackageName(), ico);
            }
                mAdapter.setIcons(icons);
            } catch (Exception e) {
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Complete = "true";
                mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
        }
        }
    }

    private class LoadGUI extends AsyncTask<App, Void, Void> {
        @Override
        protected Void doInBackground(App... apps) {
            try {
                mApps = loadInstalledApps();
            } catch (Exception e) {
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                mAdapter = new AppListAdapter(getActivity());
                mAdapter.setListItems(mApps);
                mAppsList.setAdapter(mAdapter);

                new LoadIconsTask().execute(mApps.toArray(new App[]{}));
                mAppsList.setOnItemClickListener(FavouritesList.this);
                mAppsList.setOnItemLongClickListener(FavouritesList.this);
            } catch (Exception e) {
            }
        }

    }



    private class AppSorter implements Comparator<App>
    {
        public int compare(App left, App right) {
            return left.getTitle().compareToIgnoreCase(right.getTitle());
        }
    }
}
