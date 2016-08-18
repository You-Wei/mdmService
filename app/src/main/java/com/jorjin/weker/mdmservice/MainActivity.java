package com.jorjin.weker.mdmservice;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    static final int ADMIN_INTENT = 15;
    ComponentName compName;

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        compName = new ComponentName(this, MyAdminReceiver.class);
        Intent i = new Intent(this, mdmService.class);
        //startService(i);
        setContentView(R.layout.activity_main);
        Button activate = (Button) findViewById(R.id.activate);
        activate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "enabled");
        startActivityForResult(intent, ADMIN_INTENT);
        Log.d("click", compName.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
