package com.example.cerie.callmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 拨出电话广播接收器
 */
public class TelInternalReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.i("TelInternalReceiver", "打出电话的操作广播触发,拨打的号码为：" + getResultData());
            String num = getResultData();
            Intent i = new Intent(context, TelListenerService.class);
            i.putExtra("CallNum", num);
            context.startService(i);
        }
    }
}
