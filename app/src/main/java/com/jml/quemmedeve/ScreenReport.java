package com.jml.quemmedeve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jml.quemmedeve.bean.DebtsBean;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_report);

        Bundle b = getIntent().getExtras();
        List<DebtsBean> debtsRecyclerView = b.getParcelable("debtsRecyclerView");
        totais = b.getParcelable("totais");
        dateStart = b.getString("dateStart");
        dateEnd = b.getString("dateEnd");

        lbPeriod = findViewById(R.id.lbPeriod);
        totalAll = findViewById(R.id.totalAll);
        totalSight = findViewById(R.id.totalSight);
        totalForward = findViewById(R.id.totalForward);
        salesList = findViewById(R.id.salesList);

        setHeadReport();
    }

    private void setHeadReport(){
        lbPeriod.setText("Periodo: "+dateStart+" a "+dateEnd);
        totalAll.setText("R$ "+totais.getValorTotal());
        totalSight.setText("R$ "+ totais.getValorTotalDinheiro());
        totalForward.setText("R$ "+totais.getValorTotalPrazo());
    }





}
