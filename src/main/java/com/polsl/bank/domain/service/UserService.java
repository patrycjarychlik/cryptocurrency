package com.polsl.bank.domain.service;

import com.polsl.bank.domain.BankAccount;
import com.polsl.bank.domain.User;
import com.polsl.bank.repository.AccountRepository;
import com.polsl.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

   @Autowired
   UserRepository userRepository;

    public User getUser(SecurityContext context) {
        return userRepository.findByUsername(((UserDetails) context.getAuthentication().getPrincipal()).getUsername());
    }
}
