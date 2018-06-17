package com.polsl.bank.exceptions;

public class NoSufficientCashException extends Exception {

    public NoSufficientCashException() {
        super("Bill expired or fraudulent");
    }
}