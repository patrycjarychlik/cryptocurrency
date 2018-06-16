package com.polsl.bank.domain.service;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.domain.Model.CurrencyBill;
import com.polsl.bank.domain.Bill;
import com.polsl.bank.exceptions.ExpiredOrFraudulentBill;
import com.polsl.bank.repository.AccountRepository;
import com.polsl.bank.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserService userService;

    @Autowired
    private BillRepository registerRepository;

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

    public void decreaseBalance(Long id, int value) throws Exception {
        BankAccount account = accountRepository.findOne(id);
        BigDecimal balanceAfter = account.getBalance().subtract(new BigDecimal(value));
        if (balanceAfter.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("No sufficient cash on chosen account");
        }
        account.setBalance(balanceAfter);
        accountRepository.save(account);
    }

    @Transactional
    public void deposit(CurrencyBill currencyBill) throws ExpiredOrFraudulentBill {
        Bill bill = Optional.ofNullable(registerRepository.findByBillId(currencyBill.getToken()))
                .filter(money -> currencyBill.getAmount() == money.getAmount())
                .orElseThrow(() -> new ExpiredOrFraudulentBill());
        BankAccount bankAccount = accountRepository.findOneByUser_Id(userService.getUser(SecurityContextHolder.getContext()).getId());
        increaseBalance(bankAccount.getId(), currencyBill.getAmount());
        registerRepository.delete(bill.getId());
    }

    public CurrencyBill withdraw(int amount) throws Exception {
        BankAccount bankAccount = accountRepository.findOneByUser_Id(userService.getUser(SecurityContextHolder.getContext()).getId());
        decreaseBalance(bankAccount.getId(), amount);
        Bill newBill = Bill.builder()
                .amount(amount)
                .billId(UUID.randomUUID().toString())
                .build();
        registerRepository.save(newBill);
        return CurrencyBill.builder()
                .amount(newBill.getAmount())
                .token(newBill.getBillId())
                .build();

    }

}
