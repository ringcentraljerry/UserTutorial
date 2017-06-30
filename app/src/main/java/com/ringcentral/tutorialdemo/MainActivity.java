package com.ringcentral.tutorialdemo;

import android.os.Bundle;

import com.ringcentral.usertutorial.activity.TutorialActivity;

public class MainActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
