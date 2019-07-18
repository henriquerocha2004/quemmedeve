package com.jml.quemmedeve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Reports extends AppCompatActivity {

    private RadioGroup rbgReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        rbgReport = findViewById(R.id.rbgReport);

    }
}
