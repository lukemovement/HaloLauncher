package com.paranoid.lukemovement.halolauncher;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import static android.view.View.OnClickListener;

/**
 * Created by lukepring on 28/06/13.
 */
public class BrightnessDialog extends Activity {
    /** Called when the activity is first created. */
    float BackLightValue = 0.5f; //dummy default value

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brightness_dialog);

        try {
            BackLightValue = android.provider.Settings.System.getInt(
                    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        SeekBar BackLightControl = (SeekBar)findViewById(R.id.backlightcontrol);
        Button save = (Button)findViewById(R.id.save);
        Button cancel = (Button)findViewById(R.id.cancel);

        save.setTextColor(Color.WHITE);
        cancel.setTextColor(Color.WHITE);

        save.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                int SysBackLightValue = (int)(BackLightValue * 255);
                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        SysBackLightValue);
                finish();

            }});

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        BackLightControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                // TODO Auto-generated method stub
                BackLightValue = (float)arg1/100;
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.screenBrightness = BackLightValue;
                getWindow().setAttributes(layoutParams);
            }
            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }});
    }
}