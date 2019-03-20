package com.example.life;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.test.R;

public class LifeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);
        Log.e("LifeActivity", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("LifeActivity", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("LifeActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("LifeActivity", "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("LifeActivity", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("LifeActivity", "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("LifeActivity", "onRestart");
    }
}
