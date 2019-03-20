package com.example.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MyReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "666", Toast.LENGTH_LONG).show();
            }
        }, 5000);*/

        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.test");
        MyApplication.context.registerReceiver(receiver, filter);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MyApplication.context, Main2Activity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                MyApplication.context.startActivity(intent);

                sendBroadcast(new Intent("com.android.test"));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.context.unregisterReceiver(receiver);
    }

    static class MyReceiver extends BroadcastReceiver

    {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "666", Toast.LENGTH_SHORT).show();
        }
    }
}
