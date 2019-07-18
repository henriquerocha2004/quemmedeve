package com.jml.quemmedeve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MenuPrincipal extends AppCompatActivity {


    private CardView card_clients;
    private CardView card_debtors;
    private CardView card_debts;
    private CardView card_reports;
    private CardView card_backup;
    private CardView card_options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        card_clients = findViewById(R.id.card_clients);
        card_debtors = findViewById(R.id.card_debtors);
        card_debts = findViewById(R.id.card_debt);
        card_reports = findViewById(R.id.card_reports);
        card_backup = findViewById(R.id.card_backup);
        card_options = findViewById(R.id.card_options);

        callClients();
        callDebtors();
        callReports();
        callDebts();
        callBackup();
        callOptions();
    }

    private void callClients() {
        card_clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MenuPrincipal.this, ListClients.class);
                it.putExtra("showButton", "true");
                startActivity(it);
            }
        });
    }

    private void callDebtors() {
        card_debtors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MenuPrincipal.this, ListDebtors.class);
                startActivity(it);
            }
        });
    }

    private void callDebts() {
        card_debts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MenuPrincipal.this, ListClients.class);
                it.putExtra("showButton", "false");
                startActivity(it);
            }
        });
    }

    private void callReports(){
        card_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MenuPrincipal.this, Reports.class);
                startActivity(it);
            }
        });
    }

    private void callBackup() {
    }

    private void callOptions() {
    }
}
