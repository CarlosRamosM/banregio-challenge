package com.challenge.banregio.dm.repository;

import com.challenge.banregio.dm.model.AccountStatus;
import com.challenge.banregio.dm.model.DebitAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DebitAccountRespository extends JpaRepository<DebitAccount, String> {

    @Query("SELECT a FROM DebitAccount a WHERE a.status = :status")
    List<DebitAccount> findDebitAccountsByStatus(@Param("status") final AccountStatus status);
}
