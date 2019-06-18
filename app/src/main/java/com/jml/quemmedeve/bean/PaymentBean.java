package com.jml.quemmedeve.bean;

public class PaymentBean {

    private int id;
    private int debt_id;
    private String amount_to_pay;
    private String payday;
    private int status_payment;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDebt_id() {
        return debt_id;
    }

    public void setDebt_id(int debt_id) {
        this.debt_id = debt_id;
    }

    public String getAmount_to_pay() {
        return amount_to_pay;
    }

    public void setAmount_to_pay(String amount_to_pay) {
        this.amount_to_pay = amount_to_pay;
    }

    public String getPayday() {
        return payday;
    }

    public void setPayday(String payday) {
        this.payday = payday;
    }

    public int getStatus_payment() {
        return status_payment;
    }

    public void setStatus_payment(int status_payment) {
        this.status_payment = status_payment;
    }
}
