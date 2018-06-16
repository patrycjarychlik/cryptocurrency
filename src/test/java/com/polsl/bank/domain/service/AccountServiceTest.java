/*
package com.polsl.bank.domain.service;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    private AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void createAccountWithEmptyName() {

        accountService.createAccount("");
//        verify(proofContext, times(1)).processMessage(anyObject());
    }

    @Test
    public void createAccount() {


        String name ="TestName";
        BankAccount.builder()
                .balance(0)
                .name("TestName")
                .number()
                .user()

        accountService.createAccount("");
//        assertEquals(true, proofGroupQueryModel.isDeleted());
//        verify(proofContext, times(1)).processMessage(anyObject());
    }

}
*/
