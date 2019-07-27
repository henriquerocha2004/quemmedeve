package com.jml.quemmedeve.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DebtsBean extends PaymentBean implements Parcelable {

   private int id;
   private int usu_id_debt;
   private String debt_desc;
   private String value;
   private String date_debt;
   private String debt_split;
   private String value_split;
   private int status_debt;

    //Atributos Auxiliares
    private String debtorName;
    private String remainig_value;
    private String valorTotal;
    private String valorTotalDinheiro;
    private String valorTotalPrazo;

    public DebtsBean(){}

    public DebtsBean(Parcel source) {
        id = source.readInt();
        usu_id_debt = source.readInt();
        debt_desc = source.readString();
        value = source.readString();
        date_debt = source.readString();
        debt_split = source.readString();
        value_split = source.readString();
        status_debt = source.readInt();
        debtorName = source.readString();
        remainig_value = source.readString();
        valorTotal = source.readString();
        valorTotalDinheiro = source.readString();
        valorTotalPrazo = source.readString();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(id);
        dest.writeInt(usu_id_debt);
        dest.writeString(debt_desc);
        dest.writeInt(status_debt);
        dest.writeString(value);
        dest.writeString(date_debt);
        dest.writeString(debt_split);
        dest.writeString(value_split);
        dest.writeString(debtorName);
        dest.writeString(remainig_value);
        dest.writeString(valorTotal);
        dest.writeString(valorTotalDinheiro);
        dest.writeString(valorTotalPrazo);
    }

    public static final Parcelable.Creator<DebtsBean> CREATOR = new Creator<DebtsBean>() {
        @Override
        public DebtsBean createFromParcel(Parcel source) {
            return new DebtsBean(source);
        }

        @Override
        public DebtsBean[] newArray(int size) {
           throw new UnsupportedOperationException();
        }
    };

    public String getDebtorName() { return debtorName; }

    public void setDebtorName(String debtorName) { this.debtorName = debtorName; }

    public String getValorTotal() { return valorTotal; }

    public void setValorTotal(String valorTotal) { this.valorTotal = valorTotal; }

    public String getValorTotalDinheiro() { return valorTotalDinheiro; }

    public void setValorTotalDinheiro(String valorTotalDinheiro) { this.valorTotalDinheiro = valorTotalDinheiro; }

    public String getValorTotalPrazo() { return valorTotalPrazo; }

    public void setValorTotalPrazo(String valorTotalPrazo) { this.valorTotalPrazo = valorTotalPrazo; }

    public String getRemainig_value() { return remainig_value; }

    public void setRemainig_value(String remainig_value) { this.remainig_value = remainig_value; }

    public String getValue_split() {
        return value_split;
    }

    public void setValue_split(String value_split) {
        this.value_split = value_split;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsu_id_debt() {
        return usu_id_debt;
    }

    public void setUsu_id_debt(int usu_id_debt) {
        this.usu_id_debt = usu_id_debt;
    }

    public String getDebt_desc() {
        return debt_desc;
    }

    public void setDebt_desc(String debt_desc) {
        this.debt_desc = debt_desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate_debt() {
        return date_debt;
    }

    public void setDate_debt(String date_debt) {
        this.date_debt = date_debt;
    }

    public String getDebt_split() {
        return debt_split;
    }

    public void setDebt_split(String debt_split) {
        this.debt_split = debt_split;
    }

    public int getStatus_debt() {
        return status_debt;
    }

    public void setStatus_debt(int status_debt) {
        this.status_debt = status_debt;
    }
}
