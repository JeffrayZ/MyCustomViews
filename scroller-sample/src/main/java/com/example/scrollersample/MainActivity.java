package com.example.scrollersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final View01 view = findViewById(R.id.view);
//        final View02 view = findViewById(R.id.view);
//        final View03 view = findViewById(R.id.view);
        final View04 view = findViewById(R.id.view);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * scrollBy 一步一步，点几次移动几次
                 *
                 * scrollTo 一步到位，点几次仅仅移动一次
                 * */
//                view.scrollTo(-60, -60);


                view.startScroll(-10,-10);
            }
        });
    }
}
