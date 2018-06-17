package com.polsl.bank.domain.controller;

import com.polsl.bank.domain.Interest;
import com.polsl.bank.domain.Model.InterestModel;
import com.polsl.bank.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interest")
public class InterestController {

    @Autowired
    private InterestRepository interestRepository;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<InterestModel> getAllInterest() {
        return interestRepository.findAll().stream()
                .map(interest -> InterestModel.builder()
                        .name(interest.getName())
                        .value(interest.getValue() + "%")
                        .build()).collect(Collectors.toList());
    }

    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public InterestModel updateInterest(String name, int value) {
        Interest interest = interestRepository.findOneByName(name);
        interest.setValue(value);
        interestRepository.save(interest);
        return InterestModel.builder()
                .name(interest.getName())
                .value(interest.getValue() + "%")
                .build();
    }

}
