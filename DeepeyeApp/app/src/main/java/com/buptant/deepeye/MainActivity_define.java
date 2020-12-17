package com.buptant.deepeye;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity_define extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (null == savedInstanceState){
            getFragmentManager().beginTransaction()
                    .replace(R.id.activity_main, CameraPreviewFragment_define.newInstance()).commit();
        }

    }
}
