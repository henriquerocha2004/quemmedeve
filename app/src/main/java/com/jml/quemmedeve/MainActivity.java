package com.jml.quemmedeve;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.jml.quemmedeve.adapters.Adapter;
import com.jml.quemmedeve.bean.DebtorsBean;
import com.jml.quemmedeve.controllers.ClienteController;
import com.jml.quemmedeve.database.DebtorsDbHelper;

import java.util.List;

import ru.kolotnev.formattedittext.MaskedEditText;

public class MainActivity extends AppCompatActivity {

    private Button btnAdicionarCli;
    private RecyclerView listDebtors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDebtorsList();

        btnAdicionarCli = findViewById(R.id.btnAdicionarCli);
        btnAdicionarCli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClientDialog();
            }
        });

    }

    protected void onRestart() {
        super.onRestart();
        getDebtorsList();
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
                    Intent it = new Intent(MainActivity.this, ShowDebtors.class);
                    it.putExtra("idCliente", idRow);
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

    private void getDebtorsList(){

        List<DebtorsBean> getDebtors = ClienteController.getAllClientsList(getApplicationContext());

//        if(getDebtors.getCount() > 0){
//            String[] columns = new String[] {DebtorsDbHelper.COLUMN_NAME, "total"};
//            int[] fieldsViewId = new int[] {R.id.txtNameDebtor, R.id.txtTotalDebts};
//            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list_debtors_fields, getDebtors, columns, fieldsViewId, 0);
//            if(listDebtors.getHeaderViewsCount() == 0){
//                LayoutInflater inflater = getLayoutInflater();
//                ViewGroup header = (ViewGroup) inflater.inflate(R.layout.desc_fields_list_debtors, listDebtors, false);
//                listDebtors.addHeaderView(header, null, false);
//            }
//            listDebtors.setAdapter(adapter);

            listDebtors = findViewById(R.id.listDebtors);
            listDebtors.setHasFixedSize(true);

            LinearLayoutManager ln = new LinearLayoutManager(getApplicationContext());
            listDebtors.setLayoutManager(ln);

            Adapter adp = new Adapter(getDebtors, getApplicationContext());
            listDebtors.setAdapter(adp);

            callDetailsDebtor();
//        }
    }

    private void callDetailsDebtor(){

//        listDebtors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent it = new Intent(MainActivity.this, ShowDebtors.class);
//                it.putExtra("idCliente", id);
//                startActivity(it);
//            }
//        });


    }
}
