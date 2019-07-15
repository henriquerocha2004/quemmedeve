package com.jml.quemmedeve;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jml.quemmedeve.controllers.ClienteController;
import com.jml.quemmedeve.controllers.DebtController;
import com.jml.quemmedeve.database.DebtsDbHelper;
import com.jml.quemmedeve.database.PaymentDbHelper;
import com.jml.quemmedeve.ultility.DateUltility;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import ru.kolotnev.formattedittext.DecimalEditText;

public class AddPayment extends AppCompatActivity {


    private Spinner spPaymentSplit;
    private RadioGroup rdGrpFormPay;
    private DecimalEditText txtValueFull;
    private TextView txtDescPagamento;
    private TextView datePaySplit;
    private EditText txtDescPay;
    private FloatingActionButton btnCalendar;
    private Button btnSalvar;
    private String formaPagamento;
    private String qtdParcelas;
    private Locale ptBR = new Locale("pt", "BR");
    private BigDecimal valorParcelado;
    private String idCliente;
    private CheckBox cbLembrarMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);
        Intent it = getIntent();

        idCliente = it.getStringExtra("idCliente");
        txtValueFull = findViewById(R.id.txtValueFull);
        txtDescPagamento = findViewById(R.id.txtDescPagamento);
        datePaySplit = findViewById(R.id.datePaySplit);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnSalvar = findViewById(R.id.btnSalvar);
        txtDescPay = findViewById(R.id.txtDescPay);
        cbLembrarMe = findViewById(R.id.cbLembrarMe);
        valorParcelado = new BigDecimal(0);
        qtdParcelas = "1";

        mountSpinner();
        eventFormPayment();
        eventChangeValue();
        datePicker();
        salvar();
    }


    // Função responsável por mountar o Spinner com as opções.
    private void mountSpinner(){
        spPaymentSplit = findViewById(R.id.spPaymentForm);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payments_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPaymentSplit.setEnabled(false);
        spPaymentSplit.setAdapter(adapter);
        eventCalculateSplits();
    }

    //Função responsável por gerir os radios buttons e seus eventos relacionados
    private void eventFormPayment(){
        rdGrpFormPay = findViewById(R.id.rdGrpFormPay);

        rdGrpFormPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View rdBtn = rdGrpFormPay.findViewById(checkedId);
                int index = rdGrpFormPay.indexOfChild(rdBtn);

                if(index == 0 || index == 2){
                    spPaymentSplit.setEnabled(false);
                    cbLembrarMe.setChecked(false);
                }else{
                    spPaymentSplit.setEnabled(true);
                    cbLembrarMe.setChecked(true);
                }
            }
        });
    }

    private void eventCalculateSplits(){
        spPaymentSplit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                BigDecimal valorPagar = txtValueFull.getValue();
                Object item = parent.getSelectedItem();

                if(valorPagar.toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Informe um valor para fazer o parcelamento!", Toast.LENGTH_LONG).show();
                }else if(item.toString().isEmpty()){

                }else{
                    qtdParcelas = item.toString().substring(0, (item.toString().length() == 2 ? 1 : 2));
                    valorParcelado = calcularParcelas(Integer.parseInt(qtdParcelas), valorPagar);
                    String descPagamento = String.format("Parcelado em %s de R$ %,.2f", item.toString(), valorParcelado);
                    txtDescPagamento.setText(descPagamento);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void eventChangeValue(){

        txtValueFull.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String descPagamento = null;

                if(spPaymentSplit.isEnabled() == true){
                    String itemSpinner = spPaymentSplit.getSelectedItem().toString();
                    BigDecimal valorTotal = txtValueFull.getValue();
                    qtdParcelas = itemSpinner.substring(0, itemSpinner.indexOf("x"));
                    valorParcelado = calcularParcelas(Integer.parseInt(qtdParcelas), valorTotal);
                    descPagamento = String.format("Parcelado em %s de R$ %,.2f", qtdParcelas, valorParcelado);

                }else {
                    descPagamento = String.format("Dívida de R$ %,.2f", txtValueFull.getValue());
                }

                txtDescPagamento.setText(descPagamento);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void datePicker(){

        final Calendar c = Calendar.getInstance();

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog date = new DatePickerDialog(AddPayment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        String date = String.format("%02d/%02d/%s", mDayOfMonth,mMonth + 1,mYear);
                        datePaySplit.setText(date);
                    }
                }, year, month,day);

                date.show();
            }
        });

    }

    private BigDecimal calcularParcelas(int parcelas, BigDecimal valor){
        valorParcelado = (BigDecimal) valor.divide(BigDecimal.valueOf(parcelas), MathContext.DECIMAL32);
        return valorParcelado;
    }


    private void salvar(){

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String validacao = validate();
                if(validacao != "Validado"){
                   Toast.makeText(getApplicationContext(), validacao, Toast.LENGTH_SHORT).show();
                }else{

                    ContentValues debt = new ContentValues();
                    debt.put(DebtsDbHelper.COLUMN_DEBT_DESC, txtDescPay.getText().toString());
                    debt.put(DebtsDbHelper.COLUMN_VALUE, txtValueFull.getValue().toString());
                    debt.put(DebtsDbHelper.COLUMN_DEBT_SPLIT, (formaPagamento.equals("Parcelado") ? qtdParcelas : "1"));
                    debt.put(DebtsDbHelper.COLUMN_VALUE_SPLIT,  (formaPagamento.equals("Parcelado") ? valorParcelado.toString() : txtValueFull.getValue().toString()));
                    debt.put(DebtsDbHelper.COLUMN_USU_ID, idCliente);
                    debt.put(DebtsDbHelper.COLUMN_DATE_DEBT, DateUltility.getCurrentData("USA"));


                    ContentValues payment = new ContentValues();
                    payment.put(PaymentDbHelper.COLUMN_AMOUNT_PAY, valorParcelado.toString());
                    payment.put(PaymentDbHelper.COLUMN_PAYDAY, (formaPagamento.equals("Parcelado") ? DateUltility.formataUSA(datePaySplit.getText().toString()) : DateUltility.getCurrentData("USA")));


                    if(formaPagamento.equals("A vista")){
                        debt.put(DebtsDbHelper.COLUMN_STATUS_DEBT, 1);
                        payment.put(PaymentDbHelper.COLUMN_STATUS_PAYMENT, 1);
                    }else{
                        debt.put(DebtsDbHelper.COLUMN_STATUS_DEBT, 0);
                        payment.put(PaymentDbHelper.COLUMN_STATUS_PAYMENT, 0);
                    }

                    DebtController save = new DebtController();
                    boolean result = save.store(debt, payment, getApplicationContext());
                    String mensagem = null;

                    if(result == true){

                        try {
                            if(cbLembrarMe.isChecked()){
                                Cursor cliente = ClienteController.findById(idCliente, getApplicationContext());
                                cliente.moveToFirst();
                                String[] dados = {cliente.getString(cliente.getColumnIndex("name")), debt.get(DebtsDbHelper.COLUMN_DEBT_DESC).toString(), valorParcelado.toString(), qtdParcelas};
                                Intent it = DateUltility.setEventOnCalendar(save.datas, dados);
                                startActivity(it);
                            }

                            mensagem = "Debito cadastrado com sucesso!!";
                            Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(AddPayment.this, MenuPrincipal.class);
                            startActivity(it);
                            finish();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }else{
                        mensagem = "Houve um erro ao salvar!";
                        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    private String validate(){

        int idRadio = rdGrpFormPay.getCheckedRadioButtonId();
        RadioButton rb = findViewById(idRadio);
        formaPagamento = rb.getText().toString();

        if(txtDescPay.getText().toString().isEmpty()){
            return "Informe uma Descrição para o débito";
        }

        if(txtValueFull.getValue().toString().equals("0")){
            return "Informe um valor para o débito";
        }

        if(rb.getText().toString().equals("Parcelado")){
            if(spPaymentSplit.getSelectedItem().toString().isEmpty()){
                return "Informe uma quantidade de parcelas!";
            }

            if(datePaySplit.getText().toString().isEmpty()){
                return "Informe uma data de vencimento das parcelas";
            }
        }

        if(rb.getText().toString().equals("A Prazo")){
            if(datePaySplit.getText().toString().isEmpty()){
                return "Informe uma data de vencimento das parcelas";
            }
        }


        return "Validado";
    }

}
