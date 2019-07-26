package com.jml.quemmedeve;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.jml.quemmedeve.ultility.DateUltility;

import java.util.Calendar;

public class Reports extends AppCompatActivity {

    private TextView dateStart;
    private TextView dateEnd;
    private FloatingActionButton setDateStart;
    private FloatingActionButton setDateEnd;
    private Button btnReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        dateStart = findViewById(R.id.dateStart);
        dateEnd = findViewById(R.id.dateEnd);
        setDateStart = findViewById(R.id.setDateStart);
        setDateEnd = findViewById(R.id.setDateEnd);
        btnReport = findViewById(R.id.btnReport);

        dateStart.setEnabled(false);
        dateEnd.setEnabled(false);

        generateReport();
        setPeriod();

    }

    private void setPeriod(){
        datePicker(setDateStart, dateStart);
        datePicker(setDateEnd, dateEnd);
    }


    private void generateReport(){

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    private void datePicker(FloatingActionButton button, TextView ca){

        final Calendar c = Calendar.getInstance();
        final TextView field = ca;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog date = new DatePickerDialog(Reports.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        String date = String.format("%02d/%02d/%s", mDayOfMonth,mMonth + 1,mYear);
                        field.setText(date);
                    }
                }, year, month,day);

                date.show();
            }
        });

    }

}
