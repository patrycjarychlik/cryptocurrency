package com.polsl.bank.domain.service;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

   @Autowired
    AccountRepository accountRepository;

   @Autowired
    UserService userService;

    public BankAccount createAccount(String accountName) {
        return accountRepository.save(BankAccount.builder()
                .name(accountName)
                .balance(new BigDecimal(0))
                .number(UUID.randomUUID().toString())
                .user(userService.getUser(SecurityContextHolder.getContext()))
                .build());
    }

    public void increaseBalance(Long id, int value) {
        BankAccount account = accountRepository.findOne(id);
        BigDecimal newBalance = account.getBalance().add(new BigDecimal(value));
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

}
