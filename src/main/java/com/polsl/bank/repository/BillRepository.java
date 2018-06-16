package com.polsl.bank.repository;

import com.polsl.bank.domain.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Bill findByBillId(String billId);

}