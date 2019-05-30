package com.jml.quemmedeve;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.jml.quemmedeve.database.DebtorsContract;
import com.jml.quemmedeve.database.DebtorsDbHelper;


public class MainActivity extends AppCompatActivity {

    private Button btnAdicionarCli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdicionarCli = (Button) findViewById(R.id.btnAdicionarCli);
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

        final EditText nomeCliente = (EditText) view.findViewById(R.id.txtNome);
        nomeCliente.setContentDescription("Informe o Nome");

        final EditText telefoneCliente = (EditText) view.findViewById(R.id.txtTelefone);
        telefoneCliente.setContentDescription("Informe o Telefone");

        modal.setPositiveButton("SALVAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DebtorsDbHelper debtorsHelper = new DebtorsDbHelper(getApplicationContext());
                SQLiteDatabase db = debtorsHelper.getWritableDatabase();
                ContentValues valores = new ContentValues();

                valores.put(DebtorsContract.Debtors.COLUMN_NAME, nomeCliente.getText().toString());
                valores.put(DebtorsContract.Debtors.COLUMN_PHONE, telefoneCliente.getText().toString());

                long idRow = db.insert(DebtorsContract.Debtors.TABLE_NAME, null, valores);
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
}
