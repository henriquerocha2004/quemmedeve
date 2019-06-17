package com.jml.quemmedeve;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jml.quemmedeve.controllers.DebtController;
import java.util.ArrayList;
import java.util.List;

public class DetailDebt extends AppCompatActivity {

    private long idDebt;
    private TextView txtDescDebt;
    private TextView txtDataDebt;
    private TextView txtValueTotal;
    private TextView txtNumSplits;
    private TextView txtSplitPay;
    private TextView txtRemainingValue;
    private Button btnPay;
    private List<Integer> idParcelas = new ArrayList<>();

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
        btnPay = findViewById(R.id.btnPay);

        detailsDebt();
        callPayment();
    }

    protected void onRestart() {
        super.onRestart();
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

        if(txtNumSplits.getText().equals(txtSplitPay.getText().toString())){
            btnPay.setEnabled(false);
        }


    }

    private void callPayment(){


        final List<String>  parcelasSp = new ArrayList<>();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder modal = new AlertDialog.Builder(DetailDebt.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.payment_screen, null);
                modal.setMessage("Descrição do Pagamento:");
                modal.setView(view);

                TextView valor = view.findViewById(R.id.txtValorParcela);
                final Spinner parcelas = view.findViewById(R.id.spParcelas);

                Cursor payment = DebtController.getDebtForPayment(Long.toString(idDebt) , getApplicationContext());

                valor.setText("R$ "+payment.getString(payment.getColumnIndex("amount_to_pay")));

                idParcelas.clear();
                parcelasSp.clear();


                    do{
                        idParcelas.add(payment.getInt(payment.getColumnIndex("_id")));
                        parcelasSp.add("Vencimento dia: " + payment.getString(payment.getColumnIndex("payday")));

                    }while (payment.moveToNext());

                ArrayAdapter<String> dataadapter = new ArrayAdapter<String>(DetailDebt.this, android.R.layout.simple_spinner_item, parcelasSp);
                dataadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                parcelas.setAdapter(dataadapter);


                modal.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                     String idPayment = idParcelas.get(parcelas.getSelectedItemPosition()).toString();

                     boolean update = DebtController.makePayment(getApplicationContext(), idPayment);

                     if(update == true){
                         Toast.makeText(getApplicationContext(), "Pagamento feito com sucesso", Toast.LENGTH_SHORT).show();
                         detailsDebt();
                     }else{
                         Toast.makeText(getApplicationContext(), "Houve uma falha ao realizar o pagamento!", Toast.LENGTH_SHORT).show();
                     }

                    }
                });
                modal.setNegativeButton("Cancelar", null);

                AlertDialog dialog = modal.create();
                dialog.show();

            }
        });


    }
}
