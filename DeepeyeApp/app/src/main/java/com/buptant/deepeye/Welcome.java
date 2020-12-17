package com.buptant.deepeye;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends Activity {

    private Timer timer;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_welcome);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        final Intent it = new Intent(Welcome.this, ModeActivity.class);

        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {
            	startActivity(it);
            	finish();
            }
         };
        timer.schedule(task, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        task.cancel();
    }
}

