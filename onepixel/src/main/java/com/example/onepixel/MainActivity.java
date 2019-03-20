package com.example.onepixel;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Window mWindow = getWindow();
//        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//        mWindow.setBackgroundDrawable(new ColorDrawable(Color.RED));
//        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
//        attrParams.x = 0;
//        attrParams.y = 0;
//        attrParams.height = 10;
//        attrParams.width = 10;
//        mWindow.setAttributes(attrParams);



        // 获取WindowManager服务
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);



        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        wmParams.packageName = this.getPackageName();
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SCALED
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;


        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity = Gravity.START | Gravity.TOP;

        wmParams.width = 100;
        wmParams.height = 100;
        wmParams.x = 0;
        wmParams.y = 0;

        View view = new View(this);
        view.setBackgroundColor(Color.RED);
        windowManager.addView(view, wmParams);

        finish();
    }
}
