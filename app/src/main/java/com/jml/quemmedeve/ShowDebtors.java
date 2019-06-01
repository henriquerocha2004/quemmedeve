package com.jml.quemmedeve;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jml.quemmedeve.database.DebtorsDbHelper;

public class ShowDebtors extends AppCompatActivity {

    private TextView txtNomeClient;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_debtor);
        txtNomeClient = findViewById(R.id.txtNomeClient);

        Intent it = getIntent();
        long idCliente = it.getLongExtra("idCliente", 0);

        if(idCliente != 0){
            DebtorsDbHelper helper = new DebtorsDbHelper(getApplicationContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            String[] campos = {DebtorsDbHelper.COLUMN_NAME};
            String condicao = DebtorsDbHelper.COLUMN_ID + " = ?";
            String[] args = {Long.toString(idCliente)};
            Cursor c = db.query(DebtorsDbHelper.TABLE_NAME, campos,condicao,args,null,null,null);
            c.moveToFirst();
            txtNomeClient.setText(c.getString(c.getColumnIndex("name")));
        }


    }

}
