package com.jml.quemmedeve;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.jml.quemmedeve.adapters.AdapterListDebts;
import com.jml.quemmedeve.bean.DebtsBean;
import com.jml.quemmedeve.controllers.ClienteController;
import com.jml.quemmedeve.database.DebtsDbHelper;
import com.jml.quemmedeve.ultility.NumberUtility;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;


public class ShowDebtors extends AppCompatActivity {

    private TextView txtNomeClient;
    private TextView txtValTotal;
    private String idCliente;
    private Locale ptBR = new Locale("pt", "BR");
    private RecyclerView lista;
    private String nomeCliente;
    private Button btnAdicionarDebito;
    private Button btnEfetuarPagamento;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_debtor);
        Intent it = getIntent();
        idCliente = Long.toString(it.getLongExtra("idCliente", 0));
        nomeCliente = (it.getStringExtra("nomeCliente") == null ? null : it.getStringExtra("nomeCliente"));
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
        List<DebtsBean> debitos = ClienteController.getDebtsClient(idCliente, getApplicationContext());

        try{
            if(!debitos.isEmpty()){

                lista = findViewById(R.id.listDebitos);
                lista.setHasFixedSize(true);
                lista.setClickable(true);

                LinearLayoutManager ln = new LinearLayoutManager(getApplicationContext());
                lista.setLayoutManager(ln);

                final AdapterListDebts adp = new AdapterListDebts(debitos, getApplicationContext());
                adp.setClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long idDebt = adp.getIdDebt(lista.getChildAdapterPosition(v));
                        Intent it = new Intent(ShowDebtors.this, DetailDebt.class);
                        it.putExtra("idDebt", idDebt);
                        startActivity(it);
                    }
                });

                lista.setAdapter(adp);
            }

            txtNomeClient = findViewById(R.id.txtNomeClient);
            txtValTotal = findViewById(R.id.txtValTotal);
            txtNomeClient.setText(cliente.getString(0) == null ? nomeCliente : cliente.getString(0));
            txtValTotal.setText(NumberUtility.converterBr(cliente.getString(2)));

        }finally {

        }
    }

}
