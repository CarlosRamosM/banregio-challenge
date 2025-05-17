package com.challenge.banregio.service;

import com.challenge.banregio.dm.model.AccountStatus;
import com.challenge.banregio.dm.model.DebitAccount;
import com.challenge.banregio.dm.model.Loan;
import com.challenge.banregio.dm.model.LoanStatus;
import com.challenge.banregio.dm.repository.DebitAccountRespository;
import com.challenge.banregio.dm.repository.LoanRepository;
import com.challenge.banregio.dto.PaymentRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoanPaymentService {

    private final LoanRepository loanRepository;

    private final DebitAccountRespository debitAccountRespository;

    @Transactional
    public void executePayment(final PaymentRequestDTO paymentRequest) {
        log.info("Executing payment for payment request: {}", paymentRequest);
        var interestRate = BigDecimal.valueOf(paymentRequest.interestRate());
        var fee = BigDecimal.valueOf(paymentRequest.fee());
        var businessYear = paymentRequest.businessYear();
        var accounts = debitAccountRespository.findDebitAccountsByStatus(AccountStatus.ACTIVA);
        accounts.forEach(account -> processAccountLoans(account, interestRate, fee, businessYear));
    }

    private void processAccountLoans(DebitAccount account, BigDecimal interestRate, BigDecimal fee, int businessYear) {
        var loans = loanRepository.findLoanByClientAndPaymentStatus(account.getClient(), LoanStatus.PENDIENTE);
        if (loans.isEmpty()) {
            return;
        }
        var updatedAccount = processLoansForAccount(account, loans, interestRate, fee, businessYear);
        debitAccountRespository.save(updatedAccount);
    }

    private DebitAccount processLoansForAccount(DebitAccount account, List<Loan> loans,
                                                BigDecimal interestRate, BigDecimal fee, int businessYear) {
        var availableAmount = account.getAmount();
        for (var loan : loans) {
            var payment = calculatePaymentAmount(interestRate, fee, businessYear, loan);
            if (availableAmount.compareTo(payment) > 0) {
                availableAmount = availableAmount.subtract(payment);
                loanRepository.updateStatusByClientAndId(LoanStatus.PAGADO, loan.getClient(), loan.getId());
            }
        }
        account.setAmount(availableAmount);
        return account;
    }

    private BigDecimal calculatePaymentAmount(
        BigDecimal interestRate,
        BigDecimal fee,
        int businessYear,
        final Loan loan) {
        var  currentDate = LocalDate.now();
        var plazo = ChronoUnit.DAYS.between(loan.getTransactionDate(), currentDate);
        if (plazo < 0) {
            throw new IllegalArgumentException("La fecha del préstamo no puede ser posterior a la fecha actual");
        }
        var plazoDecimal = BigDecimal.valueOf(plazo);
        var diasBY = BigDecimal.valueOf(businessYear);
        var interest = loan.getAmount()
            .multiply(plazoDecimal)
            .multiply(interestRate)
            .divide(diasBY, 2, RoundingMode.HALF_UP);
        var iva = interestRate
            .multiply(fee)
            .setScale(2, RoundingMode.HALF_UP);
        return  loan.getAmount().add(interest).add(iva);
    }
}
