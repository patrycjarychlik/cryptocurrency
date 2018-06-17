package com.polsl.bank.Email;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderImpl implements EmailSender{

    private static final Logger log = Logger.getLogger(EmailSenderImpl.class);

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderImpl(JavaMailSender javaMailSender) {
        this.javaMailSender=javaMailSender;
    }

    @Override
    public void sendEmail(String to, String title, String content) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setReplyTo("unverified@onet.pl");
            helper.setFrom("unverified@onet.pl");
            helper.setSubject(title);
            helper.setText(content, true);
            log.info("Email send: "+ to);


        } catch (MessagingException e) {
            log.error("Error while sensing email", e);
        }

        javaMailSender.send(mail);
    }
}