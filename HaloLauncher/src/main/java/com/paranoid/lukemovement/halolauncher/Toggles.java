package com.paranoid.lukemovement.halolauncher;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by lukepring on 24/06/13.
 */
public class Toggles extends Fragment {

    SharedPreferences SP;

    ImageButton battery;
    ImageButton bluetooth;
    ImageButton flashlight;
    ImageButton gps;
    ImageButton vibrate;
    ImageButton sync;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.toggles, container, false);
        SP = getActivity().getSharedPreferences(getActivity().getPackageName() + "_preferences", Context.MODE_PRIVATE);

        battery = (ImageButton)rootView.findViewById(R.id.battery);
        bluetooth = (ImageButton)rootView.findViewById(R.id.bluetooth);
        flashlight = (ImageButton)rootView.findViewById(R.id.flashlight);
        gps = (ImageButton)rootView.findViewById(R.id.gps);
        vibrate = (ImageButton)rootView.findViewById(R.id.vibrate);
        sync   = (ImageButton)rootView.findViewById(R.id.sync);

        setDimentions();
        setOnClickListeners();
        setInfo();

        return rootView;

    }

    private void setInfo() {
        battery();
        bluetooth();
    }

    private void bluetooth() {

    }

    private void battery() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getActivity().registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        float batteryPct = level;

        if(isCharging) {
            if(batteryPct == -1) {
            } else if(batteryPct == 0) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_charge_0));
            } else if(batteryPct <= 15) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_charge_15));
            } else if(batteryPct <= 28) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_charge_28));
            } else if(batteryPct <= 43) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_charge_43));
            } else if(batteryPct <= 57) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_charge_57));
            } else if(batteryPct <= 71) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_charge_71));
            } else if(batteryPct <= 85) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_charge_85));
            } else if(batteryPct <= 100) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_charge_100));
            }
        } else {
            if(batteryPct == 0) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_0));
            } else if(batteryPct <= 15) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_15));
            } else if(batteryPct <= 28) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_28));
            } else if(batteryPct <= 43) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_43));
            } else if(batteryPct <= 57) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_57));
            } else if(batteryPct <= 71) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_71));
            } else if(batteryPct <= 85) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_85));
            } else if(batteryPct <= 100) {
                battery.setImageDrawable(getResources().getDrawable(R.drawable.ic_qs_battery_100));
            }
        }
    }

    private void setDimentions() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int dimentions = (size.x/3) - (size.x/29);

        battery.setMinimumHeight(dimentions);
        battery.setMinimumWidth(dimentions);
        battery.setBackgroundColor(Color.DKGRAY);

        bluetooth.setMinimumHeight(dimentions);
        bluetooth.setMinimumWidth(dimentions);
        bluetooth.setBackgroundColor(Color.DKGRAY);

        flashlight.setMinimumHeight(dimentions);
        flashlight.setMinimumWidth(dimentions);
        flashlight.setBackgroundColor(Color.DKGRAY);

        gps.setMinimumHeight(dimentions);
        gps.setMinimumWidth(dimentions);
        gps.setBackgroundColor(Color.DKGRAY);

        vibrate.setMinimumHeight(dimentions);
        vibrate.setMinimumWidth(dimentions);
        vibrate.setBackgroundColor(Color.DKGRAY);

        sync.setMinimumHeight(dimentions);
        sync.setMinimumWidth(dimentions);
        sync.setBackgroundColor(Color.DKGRAY);


    }

    public void setOnClickListeners () {
        battery.setOnTouchListener(touch);
        bluetooth.setOnTouchListener(touch);
        flashlight.setOnTouchListener(touch);
        gps.setOnTouchListener(touch);
        vibrate.setOnTouchListener(touch);
        sync.setOnTouchListener(touch);

        flashlight.setOnClickListener(flashlightClicked);

    }

    View.OnTouchListener touch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                view.setBackgroundColor(Color.rgb(121, 112, 112));
            }
            if(event.getAction() == MotionEvent.ACTION_UP){
                view.setBackgroundColor(Color.DKGRAY);
            }
            return false;
        }
    };

    View.OnClickListener flashlightClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent("net.cactii.flash2.TOGGLE_FLASHLIGHT");
            getActivity().sendBroadcast(i);
        }
    };

}