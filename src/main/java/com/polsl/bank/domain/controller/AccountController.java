package com.polsl.bank.domain.controller;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.domain.Model.Account;
import com.polsl.bank.domain.Model.CurrencyBill;
import com.polsl.bank.domain.service.AccountService;
import com.polsl.bank.exceptions.ExpiredOrFraudulentBillException;
import com.polsl.bank.repository.AccountRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Account getBankAccounts() {
        return accountService.findUserAccount();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public BankAccount newAccount(@RequestParam("name") String name) throws Exception {
       return accountService.createAccount(name);
    }

    @PostMapping(path ="/deposit")
    @ResponseStatus(value = HttpStatus.OK)
    public void depositMoney(@RequestBody CurrencyBill currencyBill) throws ExpiredOrFraudulentBillException, NotFoundException {
         accountService.deposit(currencyBill);
    }


    @PostMapping(path ="/withdraw")
    @ResponseStatus(value = HttpStatus.OK)
    public CurrencyBill withdrawMoney(@RequestParam int amount) throws Exception {
         return accountService.withdraw(amount);
    }

    @PostMapping(path = "/transfer")
    @ResponseStatus(value = HttpStatus.OK)
    public void withdrawMoney(@RequestParam int amount, @RequestParam String username) throws Exception {
        accountService.transferCash(username, amount);
    }


}
