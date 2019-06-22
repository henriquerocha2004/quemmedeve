package com.jml.quemmedeve;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class MainActivity extends AppCompatActivity {

    private Button btnAdicionarCli;
    private RecyclerView listDebtors;
    private TextView valor;

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
        btnAdicionarCli = findViewById(R.id.btnAdicionarCli);
        btnAdicionarCli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClientDialog();
            }
        });

        getReceivables();
        getDebtorsList();

    }

    protected void onRestart() {
        super.onRestart();
        getDebtorsList();
        getReceivables();
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

    private void getDebtorsList(){

        List<DebtorsBean> getDebtors = ClienteController.getAllClientsList(getApplicationContext());

            listDebtors = findViewById(R.id.listDebtors);
            listDebtors.setHasFixedSize(true);
            listDebtors.setClickable(true);

            LinearLayoutManager ln = new LinearLayoutManager(getApplicationContext());
            listDebtors.setLayoutManager(ln);

            final AdapterListDebtors adp = new AdapterListDebtors(getDebtors, getApplicationContext());
            adp.setClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long id = adp.getIdDebtor(listDebtors.getChildAdapterPosition(v));
                    String dividaTotal = adp.getTotalValor(listDebtors.getChildAdapterPosition(v));
                    Intent it = new Intent(MainActivity.this, ShowDebtors.class);
                    it.putExtra("idCliente", id);
                    startActivity(it);
                }
            });

            listDebtors.setAdapter(adp);

    }

    private void getReceivables(){
        String valorReceber = PaymentController.receivables(getApplicationContext());
        valor.setText(NumberUtility.converterBr(valorReceber));
    }
}
