package com.example.cerie.callmonitor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 保护监听服务Service
 */
public class TelProtectService extends Service{

    @Override
    public void onCreate() {
        Intent i = new Intent(this, TelListenerService.class);
        startService(i);
        Log.i("TelProtectService", "TelProtectService.守护进程");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
