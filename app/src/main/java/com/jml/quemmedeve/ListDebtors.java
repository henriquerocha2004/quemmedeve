package com.jml.quemmedeve;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;
import com.jml.quemmedeve.adapters.AdapterListDebtors;
import com.jml.quemmedeve.bean.DebtorsBean;
import com.jml.quemmedeve.controllers.ClienteController;
import com.jml.quemmedeve.controllers.PaymentController;
import com.jml.quemmedeve.ultility.NumberUtility;

import java.util.List;
import ru.kolotnev.formattedittext.MaskedEditText;

public class ListDebtors extends AppCompatActivity {


    private RecyclerView listDebtors;
    private TextView valor;
    private TextView valorMensal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Alterando o comportamento da actionBar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        View view = getSupportActionBar().getCustomView();

        valor = view.findViewById(R.id.valor);
        valorMensal = view.findViewById(R.id.valorMensal);
        getReceivables();
        getDebtorsList();

    }

    protected void onRestart() {
        super.onRestart();
        getDebtorsList();
        getReceivables();
    }



    private void getDebtorsList(){
        List<DebtorsBean> getDebtors = ClienteController.getAllDebtors(getApplicationContext(), false, null);
        mountRecyclerViewDebtors(getDebtors);
    }

    private void getDebtorsSearchList(String value){
        List<DebtorsBean> getDebtors = ClienteController.getAllDebtors(getApplicationContext(), true, value);
        mountRecyclerViewDebtors(getDebtors);
    }

    private void mountRecyclerViewDebtors(List<DebtorsBean> debtors){

        listDebtors = findViewById(R.id.listDebtors);
        listDebtors.setHasFixedSize(true);
        listDebtors.setClickable(true);

        LinearLayoutManager ln = new LinearLayoutManager(getApplicationContext());
        listDebtors.setLayoutManager(ln);

        final AdapterListDebtors adp = new AdapterListDebtors(debtors, getApplicationContext());
        adp.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = adp.getIdDebtor(listDebtors.getChildAdapterPosition(v));
                String dividaTotal = adp.getTotalValor(listDebtors.getChildAdapterPosition(v));
                Intent it = new Intent(ListDebtors.this, ShowDebtors.class);
                it.putExtra("idCliente", id);
                startActivity(it);
            }
        });

        listDebtors.setAdapter(adp);
    }



    private void getReceivables(){
        String receitasTotal = PaymentController.receivables(getApplicationContext());
        String receitasMes = PaymentController.receivablesMonth(getApplicationContext());
        valor.setText(NumberUtility.converterBr(receitasTotal));
        valorMensal.setText(NumberUtility.converterBr(receitasMes));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.busca_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.busca).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getDebtorsSearchList(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
