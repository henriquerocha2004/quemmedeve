package com.jml.quemmedeve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jml.quemmedeve.adapters.AdapterListDebtors;
import com.jml.quemmedeve.adapters.AdapterListDebts;
import com.jml.quemmedeve.bean.DebtorsBean;
import com.jml.quemmedeve.controllers.ClienteController;

import java.util.List;

public class ListClients extends AppCompatActivity {

    private RecyclerView listClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clients);

        listClients = findViewById(R.id.listClients);

        showClientsList();

    }


    private void showClientsList(){

        List<DebtorsBean> clients = ClienteController.getAllClients(getApplicationContext());

        listClients.setHasFixedSize(true);
        listClients.setClickable(true);

        LinearLayoutManager ln = new LinearLayoutManager(getApplicationContext());
        listClients.setLayoutManager(ln);
        final AdapterListDebtors adp = new AdapterListDebtors(clients, getApplicationContext());

        adp.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = adp.getIdDebtor(listClients.getChildAdapterPosition(v));
                String nomeCliente = adp.getNameClient(listClients.getChildAdapterPosition(v));
                String telefoneCliente = adp.getTotalValor(listClients.getChildAdapterPosition(v));
                Intent it = new Intent(ListClients.this, ShowDebtors.class);
                it.putExtra("idCliente", id);
                it.putExtra("nomeCliente", nomeCliente);
                it.putExtra("telefoneCliente", telefoneCliente);
                startActivity(it);
            }
        });

        listClients.setAdapter(adp);

    }
}
