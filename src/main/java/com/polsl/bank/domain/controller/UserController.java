package com.polsl.bank.domain.controller;

import com.polsl.bank.domain.User;
import com.polsl.bank.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @PostMapping(path = "/register")
    public void AddUser(User user){
        //TODO
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public User getUser() {
        return userService.getUser(SecurityContextHolder.getContext());
    }


}
