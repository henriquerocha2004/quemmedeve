package com.jml.quemmedeve.bean;

public class DebtsBean {

   private int id;
   private int usu_id_debt;
   private String debt_desc;
   private String value;
   private String date_debt;
   private String debt_split;
   private String value_split;
   private int status_debt;

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
