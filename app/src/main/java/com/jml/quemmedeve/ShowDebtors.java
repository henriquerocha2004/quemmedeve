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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class ShowDebtors extends AppCompatActivity {

    private TextView txtNomeClient;
    private TextView txtValTotal;
    private String idCliente;
    private String dividaTotal;
    private Locale ptBR = new Locale("pt", "BR");
    private RecyclerView lista;
    private Button btnAdicionarDebito;
    private Button btnEfetuarPagamento;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_debtor);
        Intent it = getIntent();
        idCliente = Long.toString(it.getLongExtra("idCliente", 0));
        dividaTotal = it.getStringExtra("dividaTotal");
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


    private void visualizarDebito(){

//        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent it = new Intent(ShowDebtors.this, DetailDebt.class);
//                it.putExtra("idDebt", id);
//                startActivity(it);
//            }
//        });
    }

    private void checkDebtor(){

        Cursor cliente = ClienteController.findById(idCliente, getApplicationContext());
        List<DebtsBean> debitos = ClienteController.getDebtsClient(idCliente, getApplicationContext());

        System.out.println(debitos);


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
                        long idDebt = adp.getIdDebt(lista.indexOfChild(v));
                        Intent it = new Intent(ShowDebtors.this, DetailDebt.class);
                        it.putExtra("idDebt", idDebt);
                        startActivity(it);
                    }
                });

                lista.setAdapter(adp);

//                String[] collumnsBd = new String[] {DebtsDbHelper.COLUMN_DEBT_DESC, DebtsDbHelper.COLUMN_VALUE, DebtsDbHelper.COLUMN_DATE_DEBT, DebtsDbHelper.COLUMN_DEBT_SPLIT, "status_pay"};
//                int[] idFieldsView = new int[] {R.id.desc_debt,R.id.date_debt,R.id.debt_split, R.id.debt_value, R.id.txtStatus};
//
//                SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.fields_list_view_cliente, debitos,collumnsBd,idFieldsView, 0);
//                lista = findViewById(R.id.listDebitos);
//
//                if(lista.getHeaderViewsCount() == 0){
//                    LayoutInflater inflater = getLayoutInflater();
//                    ViewGroup header = (ViewGroup) inflater.inflate(R.layout.desc_fields_show_debtor, lista, false);
//                    lista.addHeaderView(header, null, false);
//                }
//
//                lista.setAdapter(adapter);

                visualizarDebito();

                txtNomeClient = findViewById(R.id.txtNomeClient);
                txtValTotal = findViewById(R.id.txtValTotal);
                txtNomeClient.setText(cliente.getString(cliente.getColumnIndex("name")));
                txtValTotal.setText(dividaTotal);
            }

        }finally {

        }
    }

}
