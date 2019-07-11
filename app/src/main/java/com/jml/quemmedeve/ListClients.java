package com.jml.quemmedeve;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.List;

import ru.kolotnev.formattedittext.MaskedEditText;

public class ListClients extends AppCompatActivity {

    private RecyclerView listClients;
    private Button btnAdicionarCli;
    private String showButton;
    private TextView lbTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clients);
        listClients = findViewById(R.id.listClients);
        Intent it = getIntent();
        showButton = it.getStringExtra("showButton");
        btnAdicionarCli = findViewById(R.id.btnAdicionarCli);
        lbTitle = findViewById(R.id.lbtitle);

        if(showButton.equals("false")){
            btnAdicionarCli.setVisibility(View.INVISIBLE);
            lbTitle.setText("Informe um cliente:");
        }

        showClientsList();

        btnAdicionarCli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClientDialog();
            }
        });

    }

    public void registerClientDialog(){

        AlertDialog.Builder modal = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.register_debtors, null);
        modal.setMessage("Informe os dados do Cliente:");
        modal.setView(view);

        final EditText nomeCliente =  view.findViewById(R.id.txtNome);
        nomeCliente.setContentDescription("Informe o Nome");

        final MaskedEditText telefoneCliente = view.findViewById(R.id.txtTelefone);
        telefoneCliente.setContentDescription("Informe o Telefone");

        modal.setPositiveButton("SALVAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                long idRow = ClienteController.store(nomeCliente.getText().toString(), telefoneCliente.getText(true).toString(), getApplicationContext());

                int duracao = Toast.LENGTH_SHORT;
                Toast toast = null;

                if(idRow > 0){
                    toast = Toast.makeText(getApplicationContext(), "Salvo com sucesso!",duracao);
                    toast.show();
                    Intent it = new Intent(ListClients.this, ShowDebtors.class);
                    it.putExtra("idCliente", idRow);
                    it.putExtra("nomeCliente", nomeCliente.getText().toString());
                    it.putExtra("telefoneCliente", telefoneCliente.getText(true).toString());
                    startActivity(it);
                }else{
                    toast =  Toast.makeText(getApplicationContext(), "Não Foi possível salvar os dados!",duracao);
                    toast.show();
                }


            }
        });

        modal.setNegativeButton("CANCELAR", null);
        AlertDialog dialog = modal.create();
        dialog.show();
    }

    private void showClientsList(){
        List<DebtorsBean> clients = ClienteController.getAllClients(getApplicationContext(), false, null);
        mountRecycleviewClients(clients);
    }

    private void showClientsSearchList(String value){
        List<DebtorsBean> clients = ClienteController.getAllClients(getApplicationContext(), true, value);
        mountRecycleviewClients(clients);
    }


    private void mountRecycleviewClients(List<DebtorsBean> clients){

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
                Intent it = null;

                if(showButton.equals("false")){
                    it = new Intent(ListClients.this, AddPayment.class);
                    it.putExtra("idCliente", Long.toString(id));
                }else{
                    it = new Intent(ListClients.this, ShowDebtors.class);
                    it.putExtra("idCliente", id);
                    it.putExtra("nomeCliente", nomeCliente);
                    it.putExtra("telefoneCliente", telefoneCliente);
                }

                startActivity(it);
            }
        });

        listClients.setAdapter(adp);
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
                showClientsSearchList(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
