package com.polsl.bank.domain.controller;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.domain.Credit;
import com.polsl.bank.domain.Interest;
import com.polsl.bank.domain.Model.CreditRequest;
import com.polsl.bank.domain.Model.CreditResponse;
import com.polsl.bank.domain.User;
import com.polsl.bank.domain.service.UserService;
import com.polsl.bank.exceptions.NoInterestSetException;
import com.polsl.bank.repository.AccountRepository;
import com.polsl.bank.repository.CreditRepository;
import com.polsl.bank.repository.InterestRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/credit")
public class CreditController {

    public static final String CREDIT = "credit";

    private CreditRepository creditRepository;

    private InterestRepository interestRepository;

    private AccountRepository accountRepository;

    private UserService userService;

    @Autowired
    public CreditController(CreditRepository creditRepository, InterestRepository interestRepository, AccountRepository accountRepository, UserService userService) {
        this.creditRepository = creditRepository;
        this.interestRepository = interestRepository;
        this.accountRepository = accountRepository;
        this.userService = userService;
    }


    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<CreditResponse> getAllCredits() {
        return creditRepository.findAll().stream()
                .map(credit -> convertCredit(credit))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public CreditResponse takeCredit(@RequestBody CreditRequest creditRequest) throws Exception {

        Interest creditPercent = Optional.ofNullable(interestRepository.findOneByName(CREDIT)).orElseThrow(() -> new NoInterestSetException("credit"));

        Credit credit = Credit.builder()
                .dueDate(creditRequest.getDueDate())
                .due(calculateInterests(creditRequest, creditPercent) + creditRequest.getValue())
                .percent(creditPercent.getValue())
                .user(userService.getUser(SecurityContextHolder.getContext()))
                .value(creditRequest.getValue())
                .build();
        creditRepository.save(credit);

        return convertCredit(credit);
    }

    @PostMapping(path = "/pay")
    @ResponseStatus(value = HttpStatus.OK)
    public CreditResponse payForCredit(@RequestParam double amount, long creditId) throws ValidationException, NotFoundException {
        User user = userService.getUser(SecurityContextHolder.getContext());
        BankAccount account = Optional.ofNullable(accountRepository.findOne(user.getId())).orElseThrow(() -> new NotFoundException("account not found"));

        if (amount > 0 && account.getBalance() >= amount) {

            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);

            Credit credit = creditRepository.findOne(creditId);
            Double afterPayment = credit.getDue() - amount;
            if (afterPayment < 0.0) {
                afterPayment = 0.0;
            }
            credit.setDue(afterPayment);
            creditRepository.save(credit);
            return convertCredit(credit);
        } else {
            throw new ValidationException("Invalid amount value");
        }
    }


    private double calculateInterests(@RequestBody CreditRequest creditRequest, Interest creditPercent) {
        return creditPercent.getValue() * 0.01 * creditRequest.getValue();
    }

    private CreditResponse convertCredit(Credit credit) {
        return CreditResponse.builder()
                .id(credit.getId())
                .dueDate(credit.getDueDate())
                .due(credit.getDue())
                .value(credit.getValue())
                .build();
    }

}
