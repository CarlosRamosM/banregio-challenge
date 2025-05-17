package com.challenge.banregio.dm.repository;

import com.challenge.banregio.dm.model.Loan;
import com.challenge.banregio.dm.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT l FROM Loan l WHERE l.client = :client AND l.paymentStatus = :payment_status")
    List<Loan> findLoanByClientAndPaymentStatus(@Param("client") final String client, @Param("payment_status") final LoanStatus loanStatus);

    @Modifying
    @Query("UPDATE Loan l SET l.paymentStatus = :status WHERE l.client = :client AND l.id = :id")
    void updateStatusByClientAndId(@Param("status") LoanStatus status, @Param("client") String client, @Param("id") long id);
}
