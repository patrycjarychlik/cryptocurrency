package com.polsl.bank.schedulers;

import com.polsl.bank.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HandleDeposit {

    public static final int MIN_1 = 1;
    public static final int MIN_3 = 3;
    public static final int MIN_6 = 6;
    public static final int MIN_12 = 12;
    @Autowired
    private DepositRepository depositRepository;


    @Scheduled(fixedRate = TimeSchedule.MINUTES_1)
    public void handleDeposit1() {
        handle(MIN_1);
    }

    @Scheduled(fixedRate = TimeSchedule.MINUTES_3)
    public void handleDeposit3() {
        handle(MIN_3);
    }

    @Scheduled(fixedRate = TimeSchedule.MINUTES_6)
    public void handleDeposit6() {
        handle(MIN_6);
    }

    @Scheduled(fixedRate = TimeSchedule.MINUTES_12)
    public void handleDeposit12() {
        handle(MIN_12);
    }

    private void handle(int duration) {
        depositRepository.findAllByDueDateAfterAndDuration(new Date(), duration).stream().forEach(deposit -> {
            double newValue = deposit.getValue() + (deposit.getValue() * 0.01 * deposit.getPercent());
            deposit.setValue(newValue);
            depositRepository.save(deposit);
        });
    }


}