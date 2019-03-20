package com.example.test;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TestNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notification);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                show();

                showForeGroundService();
            }
        });
    }

    private void showForeGroundService() {
        Intent intent = new Intent(this,MyService.class);
        startService(intent);
    }

    private static int i = 1;

    private void show() {
//        //1.从系统服务中获得通知管理器
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        // notification build
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//
//        // 构建 PendingIntent
//        PendingIntent pi = null;
//        if (intent != null) {
//            pi = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
//        mBuilder.setTicker(ticker);
//        mBuilder.setContentTitle(title);
//        mBuilder.setContentText(content);
//        //版本兼容
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
//            mBuilder.setSmallIcon(R.drawable.ic_aphla_img_logo_child);
////            mBuilder.setSmallIcon(smallIcon);
//            mBuilder.setColor(Color.parseColor("#33CFC8"));
//        } else {
//            if (!Models.isEBEN()) {
//                mBuilder.setSmallIcon(smallIcon);
//            }
//            mBuilder.setPriority(Notification.PRIORITY_HIGH);//高优先级
//            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), largeIcon));
//        }
//        if (progress >= 0) {
//            mBuilder.setProgress(100, progress, false);
//        }
//        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//        mBuilder.setAutoCancel(true);//点击后取消
//        mBuilder.setDefaults(defaults);
//        mBuilder.setWhen(System.currentTimeMillis());//设置通知时间
//        if (pi != null) {
//            mBuilder.setContentIntent(pi);
//        }
//
//        mNotificationManager.notify(id, mBuilder.build());


//        Notification.Builder mBuilder;
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    "bhtz",
//                    "保活通知",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            mNotificationManager.createNotificationChannel(channel);
//            mBuilder = new Notification.Builder(MyApplication.context, "bhtz");
//        } else {
//            mBuilder = new Notification.Builder(MyApplication.context);
//        }
//        Notification notification = mBuilder.build();
//        mNotificationManager.notify(i++, notification);


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"test");
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        managerCompat.notify(i++, builder.build());


        String text = "这里是内容";
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "bhtz",
                    "保活通知",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder = new NotificationCompat.Builder(MyApplication.context, "bhtz");
        } else {
            mBuilder = new NotificationCompat.Builder(TestNotificationActivity.this, null);
        }
        Intent intent = new Intent(this, Main2Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setTicker("有一条通知")//设置在第一个通知到达时显示在状态栏的文本
                .setSmallIcon(R.mipmap.ic_launcher)//设置小图标
                .setContentTitle("内容的标题")//设置内容标题
                .setStyle(new NotificationCompat.BigPictureStyle())//设置显示的样式
                .setContentText(text)//设置显示的内容
                .setNumber(100)//在通知的右侧设置一个数字
                .setOngoing(false)//设置是否是一个可持续的通知
                .setContentInfo("右侧文本内容")//设置右侧显示的文本，
                .setAutoCancel(true)//设置此标志将使它以便当用户点击它在面板中的通知被自动取消
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(Color.BLUE);
        Notification notification = mBuilder.build();
        mNotificationManager.notify(i++, notification);
    }
}
