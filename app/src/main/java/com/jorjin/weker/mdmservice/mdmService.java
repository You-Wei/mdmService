package com.jorjin.weker.mdmservice;

import android.app.Activity;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class mdmService extends Service implements HandlerManager.LoginCallback{

    ScheduledExecutorService scheduledExecutorService;
    ComponentName compName;


    private HandlerThread thread;


    public mdmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        compName = new ComponentName(this, MyAdminReceiver.class);

        super.onCreate();
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "enabled");

        HandlerThread thread = new HandlerThread("Thread name", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
    }

    private HandlerManager hmgm;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mdmService.this.hmgm = new HandlerManager(mdmService.this);
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StringBuilder result = new StringBuilder();
                            URL url = new URL("http://192.168.244.128/mdm.php");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setDoInput(true);
                            conn.connect();


                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }
                            System.out.println(result.toString());
                            Log.d("get", result.toString());
                            Message msg = Message.obtain();
                            msg.obj = result.toString();
                            Log.d("mdm",mdmService.this.hmgm.handler.toString());
                            mdmService.this.hmgm.handler.sendMessage(msg);

                        } catch (Exception e) {
                            Log.d("a", e.toString());
                        }
                    }
                }).start();
            }
        }, 0, 3, TimeUnit.SECONDS);
        return Service.START_STICKY;
    }

    @Override
    public void onLoginSuccess(Intent intent) {
        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    @Override
    public void onLoginFailure() {
        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
        stopSelf();
    }

}