package com.polsl.bank.schedulers;

import com.polsl.bank.Email.EmailSender;
import com.polsl.bank.domain.User;
import com.polsl.bank.domain.service.AccountService;
import com.polsl.bank.repository.AccountRepository;
import com.polsl.bank.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class HandleAccountBalanceNotification {

    private static final Logger log = Logger.getLogger(HandleAccountBalanceNotification.class);
    public static final String TITLE = "Hey, get to know your current balance!";
    public static final String SUBJECT = "Your current Balance is: ";

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailSender emailSender;

    @Scheduled(fixedRate = TimeSchedule.MINUTES_20)
    public void notifyAboutCurrentBalance() {
        userRepository.findAll().stream()
                .filter(user -> Objects.nonNull(user.getEmail()))
                .forEach(user -> {
                    sendEmail(user);
                });

    }

    @Async
    public void sendEmail(User user) {
        Optional.ofNullable(accountRepository.findOneByUser_Id(user.getId())).ifPresent(account -> {
            emailSender.sendEmail(user.getEmail(), TITLE, SUBJECT + account.getBalance());
        });
    }
}