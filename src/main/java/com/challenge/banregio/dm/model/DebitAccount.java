package com.challenge.banregio.dm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "debit_accounts")
public class DebitAccount {

    @Id
    private String client;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(columnDefinition = "status")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private AccountStatus status;
}
