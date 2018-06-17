package com.polsl.bank.schedulers;

import com.polsl.bank.domain.service.AccountService;
import com.polsl.bank.repository.AccountRepository;
import javassist.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HandleSalaries {

    private static final Logger log = Logger.getLogger(HandleSalaries.class);
    public static final int SALARY = 200;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Scheduled(fixedRate = TimeSchedule.MINUTES_20)
    public void handleSalaries() {
        accountRepository.findAll().stream().forEach(account -> {
            try {
                accountService.increaseBalance(account.getId(), SALARY);
            } catch (NotFoundException e) {
                log.error("Cannot pay salary", e);
            }
        });
    }
}