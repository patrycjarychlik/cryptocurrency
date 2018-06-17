package com.polsl.bank.repository;

import com.polsl.bank.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

   Interest findOneByName(String name);

}