package com.polsl.bank.domain.controller;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.domain.service.AccountService;
import com.polsl.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<BankAccount> getBankAccounts() {
        return accountRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public BankAccount createAccount(@RequestParam("name") String name) {
        return accountService.createAccount(name);
    }

}
