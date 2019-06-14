package com.jml.quemmedeve;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jml.quemmedeve.controllers.DebtController;

public class DetailDebt extends AppCompatActivity {

    private long idDebt;
    private TextView txtDescDebt;
    private TextView txtDataDebt;
    private TextView txtValueTotal;
    private TextView txtNumSplits;
    private TextView txtSplitPay;
    private TextView txtRemainingValue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_debt);
        Intent it = getIntent();
        idDebt = it.getLongExtra("idDebt",0);

        txtDescDebt = findViewById(R.id.txtDescDebt);
        txtDataDebt = findViewById(R.id.txtDataDebt);
        txtValueTotal = findViewById(R.id.txtValueTotal);
        txtNumSplits = findViewById(R.id.txtNumSplits);
        txtSplitPay = findViewById(R.id.txtSplitPay);
        txtRemainingValue = findViewById(R.id.txtValorRestante);

        detailsDebt();
    }

    private void detailsDebt(){
        Cursor getDebt = DebtController.getdebtAndPaymentById(Long.toString(idDebt), getApplicationContext());

        txtDescDebt.setText(getDebt.getString(getDebt.getColumnIndex("debt_desc")));
        txtDataDebt.setText(getDebt.getString(getDebt.getColumnIndex("date_debt")));
        txtValueTotal.setText("R$ "+getDebt.getString(getDebt.getColumnIndex("value")));
        txtNumSplits.setText(getDebt.getString(getDebt.getColumnIndex("debt_split")));
        txtSplitPay.setText(getDebt.getString(getDebt.getColumnIndex("parc")));
        txtRemainingValue.setText("R$ "+getDebt.getString(getDebt.getColumnIndex("valor_restante")));
    }
}
