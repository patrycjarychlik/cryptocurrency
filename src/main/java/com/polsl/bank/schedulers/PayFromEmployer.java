package com.polsl.bank.schedulers;

import com.polsl.bank.domain.service.AccountService;
import com.polsl.bank.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PayFromEmployer {

    private static final Logger log = Logger.getLogger(PayFromEmployer.class);
    private static final int MIN_20 = 1200000;
    public static final int SALARY = 200;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Scheduled(fixedRate = MIN_20)
    public void reportCurrentTime() {
        accountRepository.findAll().stream().forEach(account -> {
            accountService.increaseBalance(account.getId(), SALARY);
        });
    }
}