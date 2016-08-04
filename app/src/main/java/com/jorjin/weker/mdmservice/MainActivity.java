package com.jorjin.weker.mdmservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, mdmService.class);
        startService(i);
    }
}

