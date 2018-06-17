package com.polsl.bank.domain.controller;

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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreditControllerTest {

    public static final String CREDIT = "credit";

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserService userService;

    private CreditController creditController;

    @Before
    public void setUp() {
        creditController = new CreditController(creditRepository,  interestRepository,  accountRepository,  userService);
    }

    @Test(expected = NoInterestSetException.class)
    public void takeCreditWhenNoInterestIsSetExpectNoInterestSetException() throws Exception {

        when(interestRepository.findOneByName(CREDIT)).thenReturn(null);
        CreditRequest creditRequest = CreditRequest.builder()
                .dueDate(new Date())
                .value(2000)
                .build();

        creditController.takeCredit(creditRequest);
    }


    @Test
    public void takeCreditWith10PercentExpectCalculatedDue() throws Exception {

        when(interestRepository.findOneByName(CREDIT)).thenReturn(Interest.builder().value(10).build());
        CreditRequest creditRequest = CreditRequest.builder()
                .dueDate(new Date())
                .value(2000)
                .build();

       CreditResponse response =  creditController.takeCredit(creditRequest);
        Assert.assertEquals(Double.valueOf(2200), response.getDue() );
    }

    @Test
    public void takeCreditWith0PercentExpectNoHiddenDue() throws Exception {

        when(interestRepository.findOneByName(CREDIT)).thenReturn(Interest.builder().value(0).build());
        CreditRequest creditRequest = CreditRequest.builder()
                .dueDate(new Date())
                .value(2000)
                .build();

       CreditResponse response =  creditController.takeCredit(creditRequest);
        Assert.assertEquals(Double.valueOf(2000), response.getDue() );
    }

    @Test
    public void takeCreditWith100PercentExpectDoubleDue() throws Exception {

        when(interestRepository.findOneByName(CREDIT)).thenReturn(Interest.builder().value(100).build());
        CreditRequest creditRequest = CreditRequest.builder()
                .dueDate(new Date())
                .value(2000)
                .build();

       CreditResponse response =  creditController.takeCredit(creditRequest);
        Assert.assertEquals(Double.valueOf(4000), response.getDue() );
    }

    @Test(expected = NotFoundException.class)
    public void payingCreditWhenNoAccountIsFoundExpectsNotFoundException() throws Exception {

        when(userService.getUser(SecurityContextHolder.getContext())).thenReturn(User.builder().id(1L).build());
        when(accountRepository.findOneByUser_Id(1L)).thenReturn(null);
        when(interestRepository.findOneByName(CREDIT)).thenReturn(Interest.builder().value(100).build());
        CreditRequest creditRequest = CreditRequest.builder()
                .dueDate(new Date())
                .value(2000)
                .build();

       CreditResponse response =  creditController.payForCredit(200, 1L);
    }


}