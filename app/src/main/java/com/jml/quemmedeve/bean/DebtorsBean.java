package com.jml.quemmedeve.bean;

public class DebtorsBean {

    private int id;
    private String name;
    private String phone;
    private String valueDebt;


    public DebtorsBean(){}

    public DebtorsBean(String name, String valueDebt, String phone){
          this.name = name;
          this.valueDebt = valueDebt;
          this.phone = phone;
    }

    public String getValueDebt() {
        return valueDebt;
    }

    public void setValueDebt(String valueDebt) {
        this.valueDebt = "R$ "+valueDebt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
