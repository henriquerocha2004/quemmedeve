package com.jml.quemmedeve;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.jml.quemmedeve.controllers.ClienteController;
import com.jml.quemmedeve.database.DebtsDbHelper;

import java.text.NumberFormat;
import java.util.Locale;


public class ShowDebtors extends AppCompatActivity {

    private TextView txtNomeClient;
    private TextView txtValTotal;
    private String idCliente;
    private Locale ptBR = new Locale("pt", "BR");
    private ListView lista;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_debtor);
        txtNomeClient = findViewById(R.id.txtNomeClient);
        txtValTotal = findViewById(R.id.txtValTotal);
        Intent it = getIntent();
        idCliente = Long.toString(it.getLongExtra("idCliente", 0));

        Cursor cliente = ClienteController.findById(idCliente, getApplicationContext());
        Cursor debitos = ClienteController.getDebtsClient(idCliente, getApplicationContext());

        float valorTotal = 0;
        try{
            if(debitos != null){

                while(debitos.moveToNext()){
                    valorTotal += debitos.getFloat(1);
                }

                debitos.moveToFirst();

                txtNomeClient.setText(cliente.getString(cliente.getColumnIndex("name")));
                txtValTotal.setText(NumberFormat.getCurrencyInstance(ptBR).format(valorTotal));

                String[] collumnsBd = new String[] {DebtsDbHelper.COLUMN_ID,DebtsDbHelper.COLUMN_DEBT_DESC, DebtsDbHelper.COLUMN_VALUE, DebtsDbHelper.COLUMN_DATE_DEBT, DebtsDbHelper.COLUMN_DEBT_SPLIT};
                int[] idFieldsView = new int[] {R.id.desc_debt,R.id.date_debt,R.id.debt_split, R.id.debt_value};

                SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.show_debtor, debitos,collumnsBd,idFieldsView, 0);

                lista = findViewById(R.id.listDebitos);
                lista.setAdapter(adapter);
            }


        }finally {

        }


    }

}
