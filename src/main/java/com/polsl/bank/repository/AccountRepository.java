package com.polsl.bank.repository;

import com.polsl.bank.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Long> {

    BankAccount findOneByUser_Id(long userId);

}