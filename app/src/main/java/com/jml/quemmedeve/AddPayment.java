package com.jml.quemmedeve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPayment extends AppCompatActivity {


    private Spinner spPaymentSplit;
    private RadioGroup rdGrpFormPay;
    private EditText txtValueFull;
    private int valorParcelado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);
        mountSpinner();
        eventFormPayment();
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

        txtValueFull = findViewById(R.id.txtValueFull);

        spPaymentSplit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println(txtValueFull.getText());

                if(txtValueFull.getText().equals(null)){
                    Toast.makeText(getApplicationContext(), "Informe um valor para fazer o parcelamento!", Toast.LENGTH_LONG).show();
                }else{

                    Object item = parent.getSelectedItem();



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


}
