package com.polsl.bank.Email;

public interface EmailSender {
    void sendEmail(String to, String subject, String content);
}