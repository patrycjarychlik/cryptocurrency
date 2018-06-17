package com.polsl.bank.repository;

import com.polsl.bank.domain.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

    List<Credit> findAllByPaidIsFalse();
}