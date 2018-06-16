package com.polsl.bank.domain.service;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.repository.AccountRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SpringBootJPAIntegrationTest {

    @BeforeClass
    public static void switchOffLiquibase() {
        System.setProperty("liquibase.should.run", "false");
    }

    @Mock
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Before
    public void initTest(){
        accountRepository.save(BankAccount.builder().name("Klara").build());
    }
 
    @Test
    public void CreateAccountTest() {
        List<BankAccount> accounts = accountRepository.findAll();
        assertEquals("account size", 1,accounts.size());
    }

    @Test
    public void CreateAccount() {
//        List<BankAccount> accounts = accountService.createAccount("Firmowe");
//        assertEquals("account size", 1,accounts.size());
    }
}
