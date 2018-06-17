package com.polsl.bank.domain.service;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.domain.Bill;
import com.polsl.bank.domain.Model.Account;
import com.polsl.bank.domain.Model.CurrencyBill;
import com.polsl.bank.domain.User;
import com.polsl.bank.exceptions.DuplicatedEntityException;
import com.polsl.bank.exceptions.ExpiredOrFraudulentBillException;
import com.polsl.bank.exceptions.NoSufficientCashException;
import com.polsl.bank.repository.AccountRepository;
import com.polsl.bank.repository.BillRepository;
import com.polsl.bank.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    private UserRepository userRepository;

    private UserService userService;

    private BillRepository billRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository, UserService userService, BillRepository billRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.billRepository = billRepository;
    }

    public Account findUserAccount() {
        BankAccount account = accountRepository.findOneByUser_Id(userService.getUser(SecurityContextHolder.getContext()).getId());
        return Account.builder()
                .name(account.getName())
                .number(account.getNumber())
                .balance(account.getBalance())
                .build();
    }

    public BankAccount createAccount(String accountName) throws Exception {
        User user = userService.getUser(SecurityContextHolder.getContext());
        if (accountRepository.findOneByUser_Id(user.getId()) == null) {
            return accountRepository.save(BankAccount.builder()
                    .name(accountName)
                    .balance(0.0)
                    .number(UUID.randomUUID().toString())
                    .user(user)
                    .build());
        } else {
            throw new DuplicatedEntityException("Account already exist for this user");
        }
    }

    public void increaseBalance(Long id, double value) throws NotFoundException {
        if (value != 0) {
            BankAccount account = Optional.ofNullable(accountRepository.findOne(id)).orElseThrow(() -> new NotFoundException("account not found"));
            Double newBalance = account.getBalance() + value;
            account.setBalance(newBalance);
            accountRepository.save(account);
        }
    }

    public void decreaseBalance(Long id, double value) throws Exception {
        if (value != 0) {
            BankAccount account = Optional.ofNullable(accountRepository.findOne(id)).orElseThrow(() -> new NotFoundException("account not found"));
            Double balanceAfter = account.getBalance() - value;
            if (balanceAfter < 0.0) {
                throw new NoSufficientCashException();
            }
            account.setBalance(balanceAfter);
            accountRepository.save(account);
        }
    }

    public void decreaseBalanceWithDebit(Long id, double value) throws NotFoundException {
        BankAccount account = Optional.ofNullable(accountRepository.findOne(id)).orElseThrow(() -> new NotFoundException("account not found"));
        Double balanceAfter = account.getBalance() - value;
        account.setBalance(balanceAfter);
        accountRepository.save(account);
    }

    @Transactional
    public void deposit(CurrencyBill currencyBill) throws ExpiredOrFraudulentBillException, NotFoundException {
        Bill bill = Optional.ofNullable(billRepository.findByBillId(currencyBill.getToken()))
                .filter(money -> currencyBill.getAmount() == money.getAmount())
                .orElseThrow(() -> new ExpiredOrFraudulentBillException());
        BankAccount bankAccount = accountRepository.findOneByUser_Id(userService.getUser(SecurityContextHolder.getContext()).getId());
        increaseBalance(bankAccount.getId(), currencyBill.getAmount());
        billRepository.delete(bill.getId());
    }

    public CurrencyBill withdraw(int amount) throws Exception {
        User user = userService.getUser(SecurityContextHolder.getContext());
        BankAccount bankAccount = Optional.ofNullable(accountRepository.findOne(user.getId())).orElseThrow(() -> new NotFoundException("account not found"));
        decreaseBalance(bankAccount.getId(), amount);
        Bill newBill = Bill.builder()
                .amount(amount)
                .billId(UUID.randomUUID().toString())
                .build();
        billRepository.save(newBill);
        return CurrencyBill.builder()
                .amount(newBill.getAmount())
                .token(newBill.getBillId())
                .build();

    }

    public void transferCash(String toUserName, int amount) throws Exception {
        BankAccount source = accountRepository.findOneByUser_Id(userService.getUser(SecurityContextHolder.getContext()).getId());
        BankAccount destination = accountRepository.findOneByUser_Id(userRepository.findByUsername(toUserName).getId());
        increaseBalance(destination.getId(), amount);
        decreaseBalance(source.getId(), amount);
    }

}
