package com.jml.quemmedeve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jml.quemmedeve.adapters.AdapterReport;
import com.jml.quemmedeve.bean.DebtsBean;
import com.jml.quemmedeve.ultility.NumberUtility;

import java.util.List;

public class ScreenReport extends AppCompatActivity {

    private TextView lbPeriod;
    private TextView totalAll;
    private TextView totalSight;
    private TextView totalForward;
    private RecyclerView salesList;
    private DebtsBean totais;
    private String dateStart;
    private String dateEnd;
    private List<DebtsBean> debtsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_report);

        Intent b = getIntent();
        debtsRecyclerView = b.getParcelableArrayListExtra("debtsRecyclerView");
        totais = b.getExtras().getParcelable("totais");
        dateStart = b.getStringExtra("dateStart");
        dateEnd = b.getStringExtra("dateEnd");

        lbPeriod = findViewById(R.id.lbPeriod);
        totalAll = findViewById(R.id.totalAll);
        totalSight = findViewById(R.id.totalSight);
        totalForward = findViewById(R.id.totalForward);
        salesList = findViewById(R.id.salesList);

        setHeadReport();
        fillListSales();
    }

    private void fillListSales() {

        salesList.setHasFixedSize(true);

        LinearLayoutManager ln = new LinearLayoutManager(getApplicationContext());
        salesList.setLayoutManager(ln);
        final AdapterReport adp = new AdapterReport(debtsRecyclerView, getApplicationContext());

        salesList.setAdapter(adp);

    }

    private void setHeadReport(){
        lbPeriod.setText("Periodo: "+dateStart+" a "+dateEnd);
        totalAll.setText(NumberUtility.converterBr(totais.getValorTotal()));
        totalSight.setText(NumberUtility.converterBr(totais.getValorTotalDinheiro()));
        totalForward.setText(NumberUtility.converterBr(totais.getValorTotalPrazo()));
    }





}
