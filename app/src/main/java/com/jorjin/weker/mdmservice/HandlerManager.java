package com.jorjin.weker.mdmservice;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by weker on 8/10/2016.
 */
public class HandlerManager {

    DevicePolicyManager deviceManager;
    ActivityManager activityManager;
    ComponentName compName;
    public Handler handler;
    static final String MSG_NULL = "0";
    static final String MSG_LOCK = "1";
    static final String MSG_WIPE = "2";

    public HandlerManager(final Context c) {
        deviceManager = (DevicePolicyManager) c.getSystemService(Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        compName = new ComponentName(c, MyAdminReceiver.class);
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                Log.d("msg", msg.obj.toString());
                switch (msg.obj.toString()) {
                    case (MSG_LOCK):
                        boolean active = deviceManager.isAdminActive(compName);
                        if (active) {
                            deviceManager.lockNow();
                        } else {
                            Toast.makeText(c, "Not Registered as admin", Toast.LENGTH_SHORT).show();
                        }
                        break;

                            /*case R.id.disable:
                                deviceManager.removeActiveAdmin(compName);
                                Toast.makeText(getApplicationContext(), "Admin registration removed", Toast.LENGTH_SHORT).show();
                                break;
                                */

                    case (MSG_WIPE):
                        boolean yes = deviceManager.isAdminActive(compName);
                        if (yes) {
                            deviceManager.wipeData(0);
                        } else {
                            Toast.makeText(c, "Not Registered as admin", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case (MSG_NULL):
                        boolean ok = deviceManager.isAdminActive(compName);
                        if (ok) {

                        }
                        break;
                }
            }

        };
        Log.d("c", handler.toString());

    }



    public interface LoginCallback {
        public void onLoginSuccess(Intent intent);

        public void onLoginFailure();
    }
}


