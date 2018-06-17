package com.polsl.bank.schedulers;

import com.polsl.bank.Email.EmailSender;
import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.domain.service.AccountService;
import com.polsl.bank.repository.AccountRepository;
import com.polsl.bank.repository.CreditRepository;
import javassist.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class HandleCredits {

    private static final Logger log = Logger.getLogger(HandleCredits.class);
    public static final String TITLE = "Your credit due date passed, Sorry.";
    public static final String SUBJECT = "Penalty have been granted : ";
    public static final double PENALTY = 1.5;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private EmailSender emailSender;

    @Autowired
    private AccountService accountService;

    @Scheduled(fixedRate = TimeSchedule.MINUTES_30)
    public void handleCredits() {
        creditRepository.findAllByPaidIsFalse().stream().forEach(credit -> {

            LocalDateTime result = LocalDateTime.ofInstant(credit.getDueDate().toInstant(), ZoneId.systemDefault());
            BankAccount clientAccount = accountRepository.findOneByUser_Id(credit.getUser().getId());

            if (credit.getDue() == 0) {
                credit.setPaid(true);
                creditRepository.save(credit);

            } else if (result.isBefore(LocalDateTime.now())) {

                double penalty = credit.getValue() * Double.valueOf(PENALTY);
                try {
                    accountService.decreaseBalanceWithDebit(clientAccount.getId(), penalty);
                    emailSender.sendEmail(credit.getUser().getEmail(), TITLE, SUBJECT + penalty);
                } catch (NotFoundException e) {
                    log.error("Error while handling credits charges: " + credit.getId(), e);
                }
            }
        });
    }
}