package com.jml.quemmedeve;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jml.quemmedeve.adapters.AdapterListDebts;
import com.jml.quemmedeve.bean.DebtsBean;
import com.jml.quemmedeve.controllers.ClienteController;
import com.jml.quemmedeve.ultility.NumberUtility;
import java.util.List;
import java.util.Locale;



public class ShowDebtors extends AppCompatActivity {

    private TextView txtNomeClient;
    private TextView txtValTotal;
    private String idCliente;
    private Locale ptBR = new Locale("pt", "BR");
    private RecyclerView lista;
    private String nomeCliente;
    private String telefoneCliente;
    private TextView txtContact;
    private Button btnAdicionarDebito;
    private FloatingActionButton btnCall;
    private Button btnEfetuarPagamento;
    private static final Integer REQUEST_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_debtor);
        Intent it = getIntent();
        idCliente = Long.toString(it.getLongExtra("idCliente", 0));
        nomeCliente = (it.getStringExtra("nomeCliente") == null ? null : it.getStringExtra("nomeCliente"));
        telefoneCliente = (it.getStringExtra("telefoneCliente") == null ? null : it.getStringExtra("telefoneCliente"));
        btnAdicionarDebito = (Button) findViewById(R.id.btnAdicionarDebito);
        btnCall = findViewById(R.id.btnCall);
        adicionarDebito();
        checkDebtor();
        actionBtnDial();
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
            txtContact = findViewById(R.id.txtContact);
            txtNomeClient.setText(cliente.getString(0) == null ? nomeCliente : cliente.getString(0));
            txtContact.setText(cliente.getString(1) == null ? telefoneCliente : cliente.getString(1));
            txtValTotal.setText(NumberUtility.converterBr(cliente.getString(2)));

        }finally {

        }
    }

    //Funções Relacionadas a realizar ligações.

    private void actionBtnDial() {

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(ShowDebtors.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    makeCall();
                }else{
                    ActivityCompat.requestPermissions(ShowDebtors.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1 :{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    makeCall();
                }
            }
        }
    }

    private void makeCall(){
        final Intent callDial = new Intent(Intent.ACTION_CALL);
        callDial.setData(Uri.parse("tel:"+ txtContact.getText().toString()));
        startActivity(callDial);
    }

    //Funções Relacionadas a realizar ligações.

}
