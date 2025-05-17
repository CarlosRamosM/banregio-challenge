package com.challenge.banregio.service;

import com.challenge.banregio.dm.model.AccountStatus;
import com.challenge.banregio.dm.model.DebitAccount;
import com.challenge.banregio.dm.model.Loan;
import com.challenge.banregio.dm.model.LoanStatus;
import com.challenge.banregio.dm.repository.DebitAccountRespository;
import com.challenge.banregio.dm.repository.LoanRepository;
import com.challenge.banregio.dto.PaymentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanPaymentServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private DebitAccountRespository debitAccountRespository;

    @InjectMocks
    private LoanPaymentService loanPaymentService;

    private PaymentRequestDTO paymentRequest;
    private DebitAccount activeAccount;
    private Loan pendingLoan;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequestDTO(Float.parseFloat( "0.05"), Float.parseFloat("0.16"), 360);
        
        activeAccount = new DebitAccount();
        activeAccount.setClient("CLIENT001");
        activeAccount.setAmount(new BigDecimal("10000.00"));
        activeAccount.setStatus(AccountStatus.ACTIVA);

        pendingLoan = new Loan();
        pendingLoan.setId(1L);
        pendingLoan.setClient("CLIENT001");
        pendingLoan.setAmount(new BigDecimal("5000.00"));
        pendingLoan.setTransactionDate(LocalDate.now().minusDays(30));
        pendingLoan.setPaymentStatus(LoanStatus.PENDIENTE);
    }

    @Test
    void executePayment_WithActiveAccountsAndPendingLoans() {
        // Arrange
        List<DebitAccount> activeAccounts = List.of(activeAccount);
        List<Loan> pendingLoans = List.of(pendingLoan);

        when(debitAccountRespository.findDebitAccountsByStatus(AccountStatus.ACTIVA))
            .thenReturn(activeAccounts);
        when(loanRepository.findLoanByClientAndPaymentStatus(activeAccount.getClient(), LoanStatus.PENDIENTE))
            .thenReturn(pendingLoans);

        // Act
        loanPaymentService.executePayment(paymentRequest);

        // Assert
        verify(debitAccountRespository).findDebitAccountsByStatus(AccountStatus.ACTIVA);
        verify(loanRepository).findLoanByClientAndPaymentStatus(activeAccount.getClient(), LoanStatus.PENDIENTE);
        // No verificamos updateStatusByClientAndId porque depende del resultado de calculatePaymentAmount
        verify(debitAccountRespository).save(any(DebitAccount.class));
    }

    @Test
    void executePayment_WithNoActiveAccounts() {
        // Arrange
        when(debitAccountRespository.findDebitAccountsByStatus(AccountStatus.ACTIVA))
            .thenReturn(Collections.emptyList());

        // Act
        loanPaymentService.executePayment(paymentRequest);

        // Assert
        verify(debitAccountRespository).findDebitAccountsByStatus(AccountStatus.ACTIVA);
        verifyNoInteractions(loanRepository);
    }

    @Test
    void executePayment_WithNoPendingLoans() {
        // Arrange
        List<DebitAccount> activeAccounts = List.of(activeAccount);
        
        when(debitAccountRespository.findDebitAccountsByStatus(AccountStatus.ACTIVA))
            .thenReturn(activeAccounts);
        when(loanRepository.findLoanByClientAndPaymentStatus(activeAccount.getClient(), LoanStatus.PENDIENTE))
            .thenReturn(Collections.emptyList());

        // Act
        loanPaymentService.executePayment(paymentRequest);

        // Assert
        verify(debitAccountRespository).findDebitAccountsByStatus(AccountStatus.ACTIVA);
        verify(loanRepository).findLoanByClientAndPaymentStatus(activeAccount.getClient(), LoanStatus.PENDIENTE);
        verify(debitAccountRespository, never()).save(any(DebitAccount.class));
    }

    @Test
    void executePayment_WithInsufficientFunds() {
        // Arrange
        activeAccount.setAmount(new BigDecimal("100.00"));
        List<DebitAccount> activeAccounts = List.of(activeAccount);
        List<Loan> pendingLoans = List.of(pendingLoan);

        when(debitAccountRespository.findDebitAccountsByStatus(AccountStatus.ACTIVA))
            .thenReturn(activeAccounts);
        when(loanRepository.findLoanByClientAndPaymentStatus(activeAccount.getClient(), LoanStatus.PENDIENTE))
            .thenReturn(pendingLoans);

        // Act
        loanPaymentService.executePayment(paymentRequest);

        // Assert
        verify(debitAccountRespository).findDebitAccountsByStatus(AccountStatus.ACTIVA);
        verify(loanRepository).findLoanByClientAndPaymentStatus(activeAccount.getClient(), LoanStatus.PENDIENTE);
        verify(loanRepository, never()).updateStatusByClientAndId(
            any(LoanStatus.class), 
            anyString(), 
            anyLong()
        );
        verify(debitAccountRespository).save(any(DebitAccount.class));
    }

    @Test
    void calculatePaymentAmount_WithFutureDateLoan() {
        // Arrange
        pendingLoan.setTransactionDate(LocalDate.now().plusDays(1));
        List<DebitAccount> activeAccounts = Collections.singletonList(activeAccount);
        List<Loan> pendingLoans = Collections.singletonList(pendingLoan);

        when(debitAccountRespository.findDebitAccountsByStatus(AccountStatus.ACTIVA))
            .thenReturn(activeAccounts);
        when(loanRepository.findLoanByClientAndPaymentStatus(activeAccount.getClient(), LoanStatus.PENDIENTE))
            .thenReturn(pendingLoans);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            loanPaymentService.executePayment(paymentRequest),
            "La fecha del préstamo no puede ser posterior a la fecha actual"
        );
    }
}