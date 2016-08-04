package com.jorjin.weker.mdmservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class mdmService extends Service {

    ScheduledExecutorService scheduledExecutorService;

    public mdmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StringBuilder result = new StringBuilder();
                            URL url = new URL("http://192.168.159.128/test8.php");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setDoInput(true);
                            conn.connect();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); //creates buffer reader,.
                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }
                            System.out.println(result.toString());
                        } catch (Exception e) {
                            Log.d("a", e.toString());
                        }
                    }
                }).start();
            }
        }, 0, 3, TimeUnit.SECONDS);
        return Service.START_STICKY;
    }
}