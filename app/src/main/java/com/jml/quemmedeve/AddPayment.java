package com.jml.quemmedeve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddPayment extends AppCompatActivity {


    private Spinner spPaymentForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);
        spPaymentForm = findViewById(R.id.spPaymentForm);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payments_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPaymentForm.setAdapter(adapter);
    }
}
