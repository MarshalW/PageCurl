package com.example.PageCurl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题

        setContentView(R.layout.main);
    }
}
