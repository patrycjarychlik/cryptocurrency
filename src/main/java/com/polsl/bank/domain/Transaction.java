package com.polsl.bank.domain;


import com.polsl.bank.domain.enumeration.TransType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transid;

    private LocalDate date;

    private BigDecimal debit;

    private BigDecimal credit;

    @Enumerated(EnumType.STRING)
    private TransType type;

    private String source;

    private String destination;

    @ManyToOne
    private BankAccount bankAccount;

}
