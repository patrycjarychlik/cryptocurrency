package com.polsl.bank.schedulers;

import com.polsl.bank.domain.service.AccountService;
import com.polsl.bank.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HandleAccountFee {

    private static final Logger log = Logger.getLogger(HandleAccountFee.class);
    public static final int ACCOUNT_FEE = 5;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Scheduled(fixedRate = TimeSchedule.MINUTES_10)
    public void chargeAccounts() {
        accountRepository.findAll().stream().forEach(account -> {
            try {
                accountService.decreaseBalance(account.getId(), ACCOUNT_FEE);
            } catch (Exception e) {
                log.error("Error while charging account fee for account: " + account.getId() + " - " + account.getName(), e);
            }
        });
    }
}