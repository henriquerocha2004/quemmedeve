package com.jml.quemmedeve;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jml.quemmedeve.adapters.AdapterListDebts;
import com.jml.quemmedeve.bean.DebtsBean;
import com.jml.quemmedeve.controllers.ClienteController;
import com.jml.quemmedeve.controllers.DebtController;
import com.jml.quemmedeve.controllers.PaymentController;
import com.jml.quemmedeve.ultility.DateUltility;
import com.jml.quemmedeve.ultility.NumberUtility;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;



public class ShowDebtors extends AppCompatActivity {

    private TextView txtNomeClient;
    private TextView txtValTotal;
    private String idCliente;
    private Locale ptBR = new Locale("pt", "BR");
    private RecyclerView lista;
    private TextView txtContact;
    private Button btnAdicionarDebito;
    private Button shareDebtsPending;
    private FloatingActionButton btnCall;
    private Button btnPayAll;
    private Button btnEfetuarPagamento;
    private RadioGroup rbgMonthFilter;
    private RadioButton rbAll;
    private RadioButton rbSelectMonth;
    private TextView txtMes;
    private int rbSelectedIndex;
    private int month;
    private int year;

    private static final Integer REQUEST_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_debtor);
        Intent it = getIntent();
        idCliente = Long.toString(it.getLongExtra("idCliente", 0));

        btnAdicionarDebito = findViewById(R.id.btnAdicionarDebito);
        shareDebtsPending = findViewById(R.id.shareDebtsPending);
        btnCall = findViewById(R.id.btnCall);
        btnPayAll = findViewById(R.id.btnPayAll);
        rbAll = findViewById(R.id.rbAll);
        rbSelectMonth = findViewById(R.id.rbSelectMonth);
        rbgMonthFilter = findViewById(R.id.rbgMonthFilter);
        txtMes = findViewById(R.id.txtMes);

        adicionarDebito();
        checkDebtor(false);
        actionBtnDial();
        shareDebtsPending();
        filterPendingPayments();
        payAll();
    }


    protected void onRestart() {
        super.onRestart();
        checkDebtor(false);
    }

    private void adicionarDebito(){

        btnAdicionarDebito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ShowDebtors.this, AddPayment.class);
                it.putExtra("idCliente", idCliente);
                startActivity(it);
            }
        });
    }

    private void checkDebtor(boolean period){

        String monthYear = null;

        if(month != 0){
            monthYear = String.format("%s-%02d", year, month);
        }

        final Cursor cliente = ClienteController.findById(idCliente, getApplicationContext());
        List<DebtsBean> debitos = ClienteController.getDebtsClient(idCliente, getApplicationContext(), period, monthYear);

                lista = findViewById(R.id.listDebitos);
                lista.setHasFixedSize(true);
                lista.setClickable(true);

                LinearLayoutManager ln = new LinearLayoutManager(getApplicationContext());
                lista.setLayoutManager(ln);

                final AdapterListDebts adp = new AdapterListDebts(debitos, getApplicationContext());
                adp.setClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long idDebt = adp.getIdDebt(lista.getChildAdapterPosition(v));
                        Intent it = new Intent(ShowDebtors.this, DetailDebt.class);
                        it.putExtra("idDebt", idDebt);
                        startActivity(it);
                    }
                });

                lista.setAdapter(adp);

            txtNomeClient = findViewById(R.id.txtNomeClient);
            txtValTotal = findViewById(R.id.txtValTotal);
            txtContact = findViewById(R.id.txtContact);
            txtNomeClient.setText(cliente.getString(0));
            txtContact.setText(cliente.getString(1));
            txtValTotal.setText(NumberUtility.converterBr(cliente.getString(2)));
    }

    private void shareDebtsPending(){
        shareDebtsPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder modal = new AlertDialog.Builder(ShowDebtors.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.screen_select_period, null);
                modal.setMessage("Informe o que Deseja enviar:");
                modal.setView(view);

                final RadioGroup rbgTypeSend = view.findViewById(R.id.rbgTypeSend);
                final RadioButton rbShareMonth = view.findViewById(R.id.rbPendMonth);
                if(rbSelectedIndex == 0){
                    rbShareMonth.setEnabled(false);
                }

                modal.setPositiveButton("Gerar e Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int rdButtonId = rbgTypeSend.getCheckedRadioButtonId();
                        View radioBtn = rbgTypeSend.findViewById(rdButtonId);
                        final int indexRbSelected = rbgTypeSend.indexOfChild(radioBtn);

                        List<DebtsBean> debts = DebtController.getDebtsForDebtor(idCliente, indexRbSelected, getApplicationContext());
                        if(debts.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Não foram encontrados débitos em aberto para esse cliente!", Toast.LENGTH_SHORT).show();
                        }else{
                            shareContent(debts);
                        }
                    }
                });

                AlertDialog dialog = modal.create();
                dialog.show();
            }
        });
    }

    private void shareContent(List<DebtsBean> debts){
        double valorTotal = 0;
        String resumoDébito = "Olá, tudo bem? \n Segue abaixo os débitos ainda pendentes: \n\n";

        for(DebtsBean debt : debts){
           resumoDébito += "Dia: "+ DateUltility.formataBR(debt.getDate_debt()) + " - Pedido: "+ debt.getDebt_desc()+ " - Valor: R$ " + debt.getAmount_to_pay() + "\n";
           valorTotal += Double.parseDouble(debt.getAmount_to_pay());
        }

        resumoDébito += "\n\n Total: " + NumberUtility.converterBr(Double.toString(valorTotal));

        Intent send = new Intent();
        send.setAction(Intent.ACTION_SEND);
        send.putExtra(Intent.EXTRA_TEXT, resumoDébito);
        send.setType("text/plain");
        startActivity(send);
    }

    private void payAll(){
        btnPayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder modal = new AlertDialog.Builder(ShowDebtors.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.payment_options_all, null);
                modal.setMessage("O que deseja pagar?");
                modal.setView(view);

                final RadioGroup rbgPayall = view.findViewById(R.id.rbgPaymentAll);
                final RadioButton rbPeriodMonth = view.findViewById(R.id.rbPayThisMonth);
                if(rbSelectedIndex == 0){
                    rbPeriodMonth.setEnabled(false);
                }

                modal.setPositiveButton("Pagar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int rdButtonId = rbgPayall.getCheckedRadioButtonId();
                        View radioBtn = rbgPayall.findViewById(rdButtonId);
                        final int indexRbSelected = rbgPayall.indexOfChild(radioBtn);
                        String monthYear = String.format("%s-%02d", year, month);
                        boolean pay = PaymentController.massPayment(getApplicationContext(), indexRbSelected, monthYear, idCliente);

                        String message = "";
                        if(pay){
                             message = "Pagamentos Realizados com sucesso!";
                        }else{
                             message = "Houve uma Falha ao realizar os pagamentos!";
                        }

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        onRestart();
                    }
                });

                modal.setNegativeButton("Cancelar", null);
                AlertDialog dialog = modal.create();
                dialog.show();
            }
        });
    }

    private void filterPendingPayments(){
        rbAll.setSelected(true);
        rbgMonthFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View rdBtn = rbgMonthFilter.findViewById(checkedId);
                rbSelectedIndex = rbgMonthFilter.indexOfChild(rdBtn);

                if(rbSelectedIndex == 1){
                    showDate();
                }else{
                    checkDebtor(false);
                }
            }
        });
    }

    private void showDate(){
        final Calendar c = Calendar.getInstance();

        final int currentMonth = c.get(Calendar.MONTH);
        int currentYear = c.get(Calendar.YEAR);

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ShowDebtors.this, new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                month = selectedMonth + 1;
                year = selectedYear;
                txtMes.setText(month + "/" + year);
                checkDebtor(true);
            }
        }, currentYear, currentMonth);
        builder.build().show();
    }

    //Funções Relacionadas a realizar ligações.

    private void actionBtnDial() {

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(ShowDebtors.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    makeCall();
                }else{
                    ActivityCompat.requestPermissions(ShowDebtors.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1 :{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    makeCall();
                }
            }
        }
    }

    private void makeCall(){
        final Intent callDial = new Intent(Intent.ACTION_CALL);
        callDial.setData(Uri.parse("tel:"+ txtContact.getText().toString()));
        startActivity(callDial);
    }

    //Funções Relacionadas a realizar ligações.

}
