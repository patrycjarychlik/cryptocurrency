package com.polsl.bank.exceptions;

public class ExpiredOrFraudulentBillException extends Exception {

    public ExpiredOrFraudulentBillException() {
        super("Bill expired or fraudulent");
    }
}