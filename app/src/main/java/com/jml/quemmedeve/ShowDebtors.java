package com.jml.quemmedeve;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    private Button btnAdicionarDebito;
    private Button btnEfetuarPagamento;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_debtor);
        Intent it = getIntent();
        idCliente = Long.toString(it.getLongExtra("idCliente", 0));
        btnAdicionarDebito = (Button) findViewById(R.id.btnAdicionarDebito);
        adicionarDebito();
        checkDebtor();
    }

    protected void onRestart() {
        super.onRestart();
        checkDebtor();
    }

    private void adicionarDebito(){

        btnAdicionarDebito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ShowDebtors.this, AddPayment.class);
                it.putExtra("idCliente", idCliente);
                startActivity(it);
            }
        });
    }

    private void checkDebtor(){

        Cursor cliente = ClienteController.findById(idCliente, getApplicationContext());
        Cursor debitos = ClienteController.getDebtsClient(idCliente, getApplicationContext());

        float valorTotal = 0;
        try{
            if(debitos != null){
                if(debitos.getCount() > 0){
                    do{
                        valorTotal += debitos.getFloat(debitos.getColumnIndex("value"));
                    }while (debitos.moveToNext());
                }

                debitos.moveToFirst();


                String[] collumnsBd = new String[] {DebtsDbHelper.COLUMN_DEBT_DESC, DebtsDbHelper.COLUMN_VALUE, DebtsDbHelper.COLUMN_DATE_DEBT, DebtsDbHelper.COLUMN_DEBT_SPLIT};
                int[] idFieldsView = new int[] {R.id.desc_debt,R.id.date_debt,R.id.debt_split, R.id.debt_value};

                SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.fields_list_view_cliente, debitos,collumnsBd,idFieldsView, 0);

                lista = findViewById(R.id.listDebitos);
                lista.setAdapter(adapter);

                txtNomeClient = findViewById(R.id.txtNomeClient);
                txtValTotal = findViewById(R.id.txtValTotal);
                txtNomeClient.setText(cliente.getString(cliente.getColumnIndex("name")));
                txtValTotal.setText(NumberFormat.getCurrencyInstance(ptBR).format(valorTotal));
            }

        }finally {

        }
    }

}
