package com.polsl.bank.domain.service;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.domain.Bill;
import com.polsl.bank.domain.Model.CurrencyBill;
import com.polsl.bank.exceptions.ExpiredOrFraudulentBillException;
import com.polsl.bank.exceptions.NoSufficientCashException;
import com.polsl.bank.repository.AccountRepository;
import com.polsl.bank.repository.BillRepository;
import com.polsl.bank.repository.UserRepository;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountService(accountRepository, userRepository, userService, billRepository);
    }


    @Test(expected = ExpiredOrFraudulentBillException.class)
    public void tokenInvalidOrExpiredThenShouldReturnExpiredOrFraudulentBill() throws Exception {

        String billToken = "token1";

        when(billRepository.findByBillId("billToken")).thenReturn(null);
        CurrencyBill bill = CurrencyBill.builder().amount(100).token(billToken).build();

        accountService.deposit(bill);
    }

    @Test(expected = ExpiredOrFraudulentBillException.class)
    public void wrongAmountOfMoneyThenShouldReturnExpiredOrFraudulentBill() throws Exception {

        String billToken = "token1";

        when(billRepository.findByBillId("billToken")).thenReturn(Bill.builder().amount(1).billId("billId").build());
        CurrencyBill bill = CurrencyBill.builder().amount(100).token(billToken).build();

        accountService.deposit(bill);
    }

    @Test(expected = NotFoundException.class)
    public void AccountIsNotFoundReturnNotFoundException() throws Exception {

        Long accountId = 1L;

        when(accountRepository.findOne(accountId)).thenReturn(null);

        accountService.increaseBalance(accountId, 100);

    }

    @Test
    public void IncreaseBalanceWhenAccountExistExpectSave() throws Exception {
        Long accountId = 1L;

        when(accountRepository.findOne(accountId)).thenReturn(BankAccount.builder()
                .balance(0.0)
                .build());

        accountService.increaseBalance(accountId, 100);
        verify(accountRepository, times(1)).save(BankAccount.builder()
                .balance(Double.valueOf(100))
                .build());

    }

    @Test
    public void IncreaseBalanceWhenValueIsZeroExpectNothing() throws Exception {
        Long accountId = 1L;

        when(accountRepository.findOne(accountId)).thenReturn(BankAccount.builder()
                .balance(1.0)
                .build());
        accountService.increaseBalance(accountId, 0);
        verifyNoMoreInteractions(accountRepository);
    }


    @Test
    public void IncreaseBalanceWhenAccountExistAndBalanceBiggerThanZeroExpectValueExpectValueIncrease() throws Exception {
        Long accountId = 1L;

        when(accountRepository.findOne(accountId)).thenReturn(BankAccount.builder()
                .balance(100.0)
                .build());

        accountService.increaseBalance(accountId, 100);
        verify(accountRepository, times(1)).save(BankAccount.builder()
                .balance(Double.valueOf(200))
                .build());
    }

    @Test
    public void DecreaseBalanceWhenAccountExistAndBalanceBiggerThanZeroExpectValueIncrease() throws Exception {
        Long accountId = 1L;

        when(accountRepository.findOne(accountId)).thenReturn(BankAccount.builder()
                .balance(200.0)
                .build());

        accountService.decreaseBalance(accountId, 100);
        verify(accountRepository, times(1)).save(BankAccount.builder()
                .balance(Double.valueOf(100))
                .build());
    }

    @Test(expected = NoSufficientCashException.class)
    public void DecreaseBalanceWhenAccountNotExistExpectNoSufficientCashException() throws Exception {
        Long accountId = 1L;

        when(accountRepository.findOne(accountId)).thenReturn(BankAccount.builder()
                .balance(1.0)
                .build());
        accountService.decreaseBalance(accountId, 100);
    }

    @Test
    public void DecreaseBalanceWhenValueIsZeroExpectNothing() throws Exception {
        Long accountId = 1L;

        when(accountRepository.findOne(accountId)).thenReturn(BankAccount.builder()
                .balance(1.0)
                .build());
        accountService.decreaseBalance(accountId, 0);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    public void DecreaseBalanceWithDebitValueIsZeroExpectNothing() throws Exception {
        Long accountId = 1L;

        when(accountRepository.findOne(accountId)).thenReturn(BankAccount.builder()
                .balance(0.0)
                .build());
        accountService.decreaseBalanceWithDebit(accountId, 100);
        verify(accountRepository, times(1)).save(BankAccount.builder()
                .balance(Double.valueOf(-100))
                .build());
    }

    @Test(expected = NotFoundException.class)
    public void DecreaseBalanceWithDebitWhenAccountNotPresentExpectNotFoundException() throws Exception {
        Long accountId = 1L;

        when(accountRepository.findOne(accountId)).thenReturn(null);
        accountService.decreaseBalanceWithDebit(accountId, 100);

    }





}