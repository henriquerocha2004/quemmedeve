package com.jml.quemmedeve;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;
import java.util.Locale;

import ru.kolotnev.formattedittext.DecimalEditText;

public class AddPayment extends AppCompatActivity {


    private Spinner spPaymentSplit;
    private RadioGroup rdGrpFormPay;
    private DecimalEditText txtValueFull;
    private TextView txtDescPagamento;
    private EditText datePaySplit;
    private Locale ptBR = new Locale("pt", "BR");
    private int valorParcelado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);

        txtValueFull = findViewById(R.id.txtValueFull);
        txtDescPagamento = findViewById(R.id.txtDescPagamento);
        datePaySplit = findViewById(R.id.datePaySplit);

        mountSpinner();
        eventFormPayment();
        eventChangeValue();
        datePicker();
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

                if(index == 0){
                    spPaymentSplit.setEnabled(false);
                }else{
                    spPaymentSplit.setEnabled(true);
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
                    int parcela = Integer.parseInt(item.toString().substring(0, (item.toString().length() == 2 ? 1 : 2)));
                    BigDecimal valor = calcularParcelas(parcela, valorPagar);
                    String descPagamento = String.format("Parcelado em %s de R$ %,.2f", item.toString(), valor);
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
                    String qtdParcelas = itemSpinner.substring(0, itemSpinner.indexOf("x"));
                    BigDecimal valorParcelado = calcularParcelas(Integer.parseInt(qtdParcelas), valorTotal);
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

        datePaySplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog date = new DatePickerDialog(AddPayment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        datePaySplit.setText(mDayOfMonth + '/' + mMonth + '/' + mYear);
                    }
                }, day,month, year);

                date.show();
            }
        });

    }



    private BigDecimal calcularParcelas(int parcelas, BigDecimal valor){
        BigDecimal resultado;
        resultado = (BigDecimal) valor.divide(BigDecimal.valueOf(parcelas), MathContext.DECIMAL32);
        return resultado;
    }

}
