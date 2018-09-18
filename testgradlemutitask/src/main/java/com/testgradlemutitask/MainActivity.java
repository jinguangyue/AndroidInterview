package com.testgradlemutitask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flavor1 flavor1 = new flavor1(this);
        flavor2 flavor2 = new flavor2();
        flavor3 flavor3 = new flavor3();
    }
}
