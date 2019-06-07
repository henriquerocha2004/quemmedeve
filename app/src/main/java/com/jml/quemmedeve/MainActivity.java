package com.jml.quemmedeve;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jml.quemmedeve.controllers.ClienteController;

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


        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setData(CalendarContract.Events.CONTENT_URI);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, "Evento do Jamelao");
        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Beach House");
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "A Pig Roast on the Beach");
        calIntent.putExtra(CalendarContract.Events.RRULE, "FREQ=MONTHLY;INTERVAL=1;BYMONTHDAY=7;UNTIL=20190907T000000Z");
        startActivity(calIntent);


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

                long idRow = ClienteController.store(nomeCliente.getText().toString(), telefoneCliente.getText().toString(), getApplicationContext());

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
