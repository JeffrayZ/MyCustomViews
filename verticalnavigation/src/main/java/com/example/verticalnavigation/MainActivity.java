package com.example.verticalnavigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements VerticalNavigation.OnPageChangeListener {

    private VerticalNavigation mMianLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMianLayout = findViewById(R.id.id_main_ly);
        mMianLayout.setOnPageChangeListener(this);
    }

    @Override
    public void onPageChange(int currentPage) {
        Toast.makeText(MainActivity.this, "第"+(currentPage+1)+"页", Toast.LENGTH_SHORT).show();
    }
}
