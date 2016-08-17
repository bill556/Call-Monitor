package com.example.cerie.callmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this, TelListenerService.class);
        i.putExtra("CallNum","");
        startService(i);
        Toast.makeText(this, "服务启动!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
