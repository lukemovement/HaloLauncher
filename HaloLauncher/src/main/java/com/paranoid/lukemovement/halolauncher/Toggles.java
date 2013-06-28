package com.paranoid.lukemovement.halolauncher;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lukepring on 24/06/13.
 */
public class Toggles extends Fragment {

    SharedPreferences SP;

    LinearLayout buttonHolder;

    ImageView battery;
    ImageView brightness;
    ImageView settings;
    TextView time;
    TextView date;

    LinearLayout wifiLayout;
    ToggleButton wifiToggle;

    LinearLayout mobileLayout;
    ToggleButton mobileToggle;

    LinearLayout bluetoothLayout;
    ToggleButton blueToothToggle;

    LinearLayout autoRotateLayout;
    ToggleButton autoRotateToggle;

    LinearLayout gpsLayout;
    ToggleButton gpsToggle;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.toggles, container, false);
        SP = getActivity().getSharedPreferences(getActivity().getPackageName() + "_preferences", Context.MODE_PRIVATE);

        buttonHolder = (LinearLayout)rootView.findViewById(R.id.buttonHolder);
        buttonHolder.setBackgroundColor(Color.BLACK);

        battery = (ImageView)rootView.findViewById(R.id.battery);
        brightness = (ImageView)rootView.findViewById(R.id.brightness);
        settings = (ImageView)rootView.findViewById(R.id.settings);
        time = (TextView)rootView.findViewById(R.id.time);
        date = (TextView)rootView.findViewById(R.id.date);

        wifiToggle = (ToggleButton)rootView.findViewById(R.id.wifiToggle);
        wifiLayout = (LinearLayout)rootView.findViewById(R.id.wifiLayout);

        mobileToggle = (ToggleButton)rootView.findViewById(R.id.mobileToggle);
        mobileLayout = (LinearLayout)rootView.findViewById(R.id.mobileLayout);

        blueToothToggle = (ToggleButton)rootView.findViewById(R.id.blueToothToggle);
        bluetoothLayout = (LinearLayout)rootView.findViewById(R.id.bluetoothLayout);

        autoRotateToggle = (ToggleButton)rootView.findViewById(R.id.autoRotateToggle);
        autoRotateLayout = (LinearLayout)rootView.findViewById(R.id.autoRotateLayout);

        gpsToggle = (ToggleButton)rootView.findViewById(R.id.gpsToggle);
        gpsLayout = (LinearLayout)rootView.findViewById(R.id.gpsLayout);

        setInfo();

        return rootView;

    }

    private void setInfo() {
        try {
            battery();
        } catch (Exception e) {
            battery.setVisibility(View.GONE);
        }

        try {
            timedate();
        } catch (Exception e) {
            time.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
        }

        try {
            wifi();
        } catch (Exception e) {
            wifiLayout.setVisibility(View.GONE);
        }

        try {
            mobileData();
        } catch (Exception e) {
            mobileLayout.setVisibility(View.GONE);
        }

        try {
            bluetooth();
        } catch (Exception e) {
            bluetoothLayout.setVisibility(View.GONE);
        }

        try {
            brightness();
        } catch (Exception e) {
            brightness.setVisibility(View.GONE);
        }

        try {
            settings();
        } catch (Exception e) {
            settings.setVisibility(View.GONE);
        }

        try {
            autoRotate();
        } catch (Exception e) {
            autoRotateLayout.setVisibility(View.GONE);
        }

        try {
            gps();
        } catch (Exception e) {
            gpsLayout.setVisibility(View.GONE);
        }
    }

    private void gps() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            gpsToggle.setChecked(true);
        }

        gpsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                    final Intent poke = new Intent();
                    poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                    poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                    poke.setData(Uri.parse("3"));
                    getActivity().sendBroadcast(poke);
            }
        });
    }

    private void autoRotate() {

        try {
            if (Settings.System.getInt(getActivity().getContentResolver(),Settings.System.ACCELEROMETER_ROTATION) == 1) {
               autoRotateToggle.setChecked(true);
            }

            autoRotateToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                    Settings.System.putInt(getActivity().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, isChecked ? 1 : 0);
                }
            });
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void settings() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS).addFlags(0x00002000
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }

    private void brightness() {
        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BrightnessDialog.class));
            }
        });
    }

    private void bluetooth() {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            blueToothToggle.setChecked(true);
        }

        blueToothToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(isChecked)
                {
                    mBluetoothAdapter.enable();
                }
                else
                {
                    mBluetoothAdapter.disable();
                }
            }
        }) ;
    }

    private void wifi() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            wifiToggle.setChecked(true);
        }

        wifiToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                    WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(isChecked);
            }
        }) ;

    }

    private void mobileData() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mMobile.isConnected()) {
            mobileToggle.setChecked(true);
        }

        mobileToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
            try{
                final ConnectivityManager conman = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                final Class conmanClass = Class.forName(conman.getClass().getName());
                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                iConnectivityManagerField.setAccessible(true);
                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                setMobileDataEnabledMethod.setAccessible(true);

                setMobileDataEnabledMethod.invoke(iConnectivityManager, isChecked);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            }
        }) ;

    }

    private void timedate() {
        SimpleDateFormat timeInfo = new SimpleDateFormat("HH:mm");
        time.setText(timeInfo.format(new Date()));
        time.setTextColor(Color.WHITE);

        SimpleDateFormat dateInfo = new SimpleDateFormat("dd/MM/yy");
        date.setText(dateInfo.format(new Date()));
        date.setTextColor(Color.WHITE);
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
}