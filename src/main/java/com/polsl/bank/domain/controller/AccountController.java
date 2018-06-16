package com.polsl.bank.domain.controller;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.domain.Model.CurrencyBill;
import com.polsl.bank.domain.service.AccountService;
import com.polsl.bank.exceptions.ExpiredOrFraudulentBill;
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

//    @PutMapping
//    @ResponseStatus(value = HttpStatus.OK)
//    public BankAccount changeAccountName(@RequestParam("name") String name){
//       return accountService.createAccount(name);
//    }

    @PutMapping(path ="/deposit")
    @ResponseStatus(value = HttpStatus.OK)
    public void depositMoney(@RequestBody CurrencyBill currencyBill) throws ExpiredOrFraudulentBill {
         accountService.deposit(currencyBill);
    }


    @PutMapping(path ="/withdraw")
    @ResponseStatus(value = HttpStatus.OK)
    public CurrencyBill withdrawMoney(int amount) throws Exception {
         return accountService.withdraw(amount);
    }


}
