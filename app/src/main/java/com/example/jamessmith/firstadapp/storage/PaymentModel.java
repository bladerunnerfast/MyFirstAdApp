package com.example.jamessmith.firstadapp.storage;

/**
 * Created by James Smith on 2/17/2017.
 */

public class PaymentModel {

    private int orderCode;
    private String paid;
    private String paymentDate;
    private String expireDate;
    private String username;

    public PaymentModel( String paid, String paymentDate, int orderCode, String expireDate, String username) {
        this.paid = paid;
        this.paymentDate = paymentDate;
        this.orderCode = orderCode;
        this.expireDate = expireDate;
        this.username = username;
    }

    public PaymentModel(){}

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
