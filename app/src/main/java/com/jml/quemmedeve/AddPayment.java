package com.jml.quemmedeve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Locale;

import ru.kolotnev.formattedittext.DecimalEditText;

public class AddPayment extends AppCompatActivity {


    private Spinner spPaymentSplit;
    private RadioGroup rdGrpFormPay;
    private DecimalEditText txtValueFull;
    private TextView txtDescPagamento;
    private Locale ptBR = new Locale("pt", "BR");
    private int valorParcelado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);

        txtValueFull = findViewById(R.id.txtValueFull);
        txtDescPagamento = findViewById(R.id.txtDescPagamento);

        mountSpinner();
        eventFormPayment();
        eventChangeValue();
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

                if(valorPagar == null){
                    Toast.makeText(getApplicationContext(), "Informe um valor para fazer o parcelamento!", Toast.LENGTH_LONG).show();
                }else{
                    Object item = parent.getSelectedItem();
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

                System.out.println(spPaymentSplit.isEnabled());

                if(spPaymentSplit.isEnabled() == true){
                    String itemSpinner = spPaymentSplit.getSelectedItem().toString();
                    BigDecimal valorTotal = new BigDecimal(s.toString());
                    String qtdParcelas = itemSpinner.substring(0, itemSpinner.indexOf("x") - 1);
                    BigDecimal valorParcelado = calcularParcelas(Integer.parseInt(qtdParcelas), valorTotal);
                    descPagamento = String.format("Parcelado em %s de R$ %,.2f", qtdParcelas, valorParcelado);

                }else {
                    BigDecimal valorUnico = new BigDecimal(s.toString());
                    descPagamento = String.format("Parcelado em 1x de R$ %,.2f", valorUnico);
                }

                txtDescPagamento.setText(descPagamento);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private BigDecimal calcularParcelas(int parcelas, BigDecimal valor){
        BigDecimal resultado;
        resultado = (BigDecimal) valor.divide(BigDecimal.valueOf(parcelas), MathContext.DECIMAL32);
        return resultado;
    }

}
