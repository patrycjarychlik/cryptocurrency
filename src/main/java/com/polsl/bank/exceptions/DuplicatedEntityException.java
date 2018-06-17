package com.polsl.bank.exceptions;

public class DuplicatedEntityException extends Exception {

    public DuplicatedEntityException(String msg) {
        super(msg);
    }
}