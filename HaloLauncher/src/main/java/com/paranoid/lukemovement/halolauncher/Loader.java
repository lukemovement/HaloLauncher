package com.paranoid.lukemovement.halolauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by lukepring on 26/06/13.
 */
public class Loader extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, MainActivity.class).addFlags(0x00002000
                | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
}
