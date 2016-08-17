package com.example.cerie.callmonitor;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelListenerService extends Service {
    // 电话管理器
    private TelephonyManager telephonyManager;
    // 监听器对象
    private MyListener listener;
    //声明录音机
    private MediaRecorder mediaRecorder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 服务创建的时候调用的方法
     */
    @Override
    public void onCreate() {
        // 后台监听电话的呼叫状态。
        // 得到电话管理器
        telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    String callNum;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callNum = intent.getStringExtra("CallNum");
        Log.i("TelListenerService","onStartCommand"+callNum);
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyListener extends PhoneStateListener {
        String inComingNumber;
        // 当电话的呼叫状态发生变化的时候调用的方法
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE://空闲状态。
                        if(mediaRecorder!=null){
                            //8.停止捕获
                            mediaRecorder.stop();
                            //9.释放资源
                            mediaRecorder.release();
                            mediaRecorder = null;
                            //TODO 这个地方你可以将录制完毕的音频文件上传到服务器，这样就可以监听了
                            Log.i("TelProtectService", "音频文件录制完毕，可以在后台上传到服务器");
                        }

                        break;
                    case TelephonyManager.CALL_STATE_RINGING://零响状态。
                        inComingNumber = incomingNumber;
                            Log.i("CALL_STATE_RINGING", incomingNumber);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                        //开始录音
                        //1.实例化一个录音机
                        mediaRecorder = new MediaRecorder();
                        //2.指定录音机的声音源
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                        //3.设置录制的文件输出的格式
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                        long time=System.currentTimeMillis();
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date=new Date(time);
                        String time1=format.format(date);
                        //4.指定录音文件的名称
                        File file;
                        if ("".equals(callNum)){
                            Log.i("TelListenerService","来电电话：" + inComingNumber);
                            file = new File(Environment.getExternalStorageDirectory(),inComingNumber+"-"+time1+".3gp");
                        }else {
                            Log.i("TelListenerService","拨出电话：" + callNum);
                            file = new File(Environment.getExternalStorageDirectory(),callNum+"-"+time1+".3gp");
                            callNum = "";
                        }
                        mediaRecorder.setOutputFile(file.getAbsolutePath());
                        //5.设置音频的编码
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                        //6.准备开始录音
                        mediaRecorder.prepare();
                        //7.开始录音
                        mediaRecorder.start();
                        Log.i("TelListenerService", "mediaRecorder.start");
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 服务销毁的时候调用的方法
     * 保护监听服务
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消电话的监听,采取线程守护的方法，当一个服务关闭后，开启另外一个服务，除非你很快把两个服务同时关闭才能完成
        Intent i = new Intent(this,TelProtectService.class);
        startService(i);
        listener = null;
    }
}