package com.polsl.bank.Email;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailSenderImplTest {

    public static final String OWNER_EMAIL = "unverified@onet.pl";
    private EmailSenderImpl emailSenderImpl;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Before
    public void setUp() {
        emailSenderImpl = new EmailSenderImpl(javaMailSender);
    }


    @Test
    public void testSendingEmails() throws Exception {

        String to = "to@email.com";
        String title = "title";
        String content = "content";

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(to);
        helper.setReplyTo(OWNER_EMAIL);
        helper.setFrom(OWNER_EMAIL);
        helper.setSubject(title);
        helper.setText(content, true);

        emailSenderImpl.sendEmail(to, title, content);
        verify(javaMailSender, times(1)).send(mail);
    }

    @Test(expected = AddressException.class)
    public void testSendingEmailsWhenNotAddress() throws Exception {

        String to = "";
        String title = "title";
        String content = "content";

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(to);
        helper.setReplyTo(OWNER_EMAIL);
        helper.setFrom(OWNER_EMAIL);
        helper.setSubject(title);
        helper.setText(content, true);

        emailSenderImpl.sendEmail(to, title, content);
        verify(javaMailSender, times(1)).send(mail);
    }

    @Test
    public void testSendingEmailsWhenNoTitleExpectToSendRegardless() throws Exception {

        String to =  "to@email.com";
        String title = "";
        String content = "content";

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(to);
        helper.setReplyTo(OWNER_EMAIL);
        helper.setFrom(OWNER_EMAIL);
        helper.setSubject(title);
        helper.setText(content, true);

        emailSenderImpl.sendEmail(to, title, content);
        verify(javaMailSender, times(1)).send(mail);
    }


}