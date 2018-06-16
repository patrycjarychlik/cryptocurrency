package com.polsl.bank.exceptions;

public class ExpiredOrFraudulentBill extends Exception {

    public ExpiredOrFraudulentBill() {
        super("Bill expired or fraudulent");
    }
}