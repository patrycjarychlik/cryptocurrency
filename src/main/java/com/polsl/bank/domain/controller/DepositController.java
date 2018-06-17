package com.polsl.bank.domain.controller;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.domain.Deposit;
import com.polsl.bank.domain.Interest;
import com.polsl.bank.domain.Model.DepositRequest;
import com.polsl.bank.domain.Model.DepositResponse;
import com.polsl.bank.domain.service.UserService;
import com.polsl.bank.repository.AccountRepository;
import com.polsl.bank.repository.DepositRepository;
import com.polsl.bank.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/investment")
public class DepositController {

    public static final String INVESTMENT = "investment";

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    UserService userService;

    @Autowired
    AccountRepository accountRepository;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<DepositResponse> getAll() {
        return depositRepository.findAll().stream()
                .map(deposit -> convert(deposit))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public DepositResponse putDeposit(@RequestBody DepositRequest request) throws Exception {

        BankAccount account = accountRepository.findOneByUser_Id(userService.getUser(SecurityContextHolder.getContext()).getId());
        Interest percent = Optional.ofNullable(interestRepository.findOneByName(INVESTMENT)).orElseThrow(() -> new Exception("No investment interest set."));
        if (account.getBalance() >= request.getValue()) {


            account.setBalance(account.getBalance() - request.getValue());
            accountRepository.save(account);

            Deposit deposit = Deposit.builder()
                    .dueDate(request.getDueDate())
                    .percent(percent.getValue())
                    .user(userService.getUser(SecurityContextHolder.getContext()))
                    .value(request.getValue())
                    .autoRenewal(request.isAutoRenewal())
                    .duration(request.getDuration())
                    .build();

            depositRepository.save(deposit);

            return convert(deposit);
        } else {
            throw new Exception("Amount is bigger than your balance");
        }
    }

    private DepositResponse convert(Deposit deposit) {
        return DepositResponse.builder()
                .dueDate(deposit.getDueDate())
                .duration(deposit.getDuration())
                .value(deposit.getValue())
                .id(deposit.getId())
                .build();
    }


}
