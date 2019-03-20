package com.example.alarmManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.test.R;

public class AlarmManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);




        int i = 1/0;



//        Intent intent = new Intent(this, Main2Activity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
//                Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent restartIntent = PendingIntent.getActivity(
//                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
    }
}
