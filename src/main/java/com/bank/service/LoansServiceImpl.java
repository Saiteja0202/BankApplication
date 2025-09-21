package com.bank.service;

import com.bank.exception.LoanNotFoundException;
import com.bank.exception.UserNotFoundException;
import com.bank.model.*;
import com.bank.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
@Service
public class LoansServiceImpl implements LoansService {

    private final LoansRepo loansRepo;
    private final UsersRepo usersRepo;
    private final AccountsRepo accountsRepo;
    private final TransactionsRepo transactionsRepo;

    public LoansServiceImpl(LoansRepo loansRepo, UsersRepo usersRepo,
                            AccountsRepo accountsRepo, TransactionsRepo transactionsRepo) {
        this.loansRepo = loansRepo;
        this.usersRepo = usersRepo;
        this.accountsRepo = accountsRepo;
        this.transactionsRepo = transactionsRepo;
    }

    @Override
    public String requestLoan(Integer userId, Loans.LoanType loanType, BigDecimal amount, Integer tenureMonths) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        BigDecimal annualRate = getInterestRate(loanType);
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

        BigDecimal numerator = amount.multiply(monthlyRate).multiply(
                BigDecimal.ONE.add(monthlyRate).pow(tenureMonths)
        );
        BigDecimal denominator = (BigDecimal.ONE.add(monthlyRate).pow(tenureMonths)).subtract(BigDecimal.ONE);
        BigDecimal emi = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        BigDecimal totalPayable = emi.multiply(BigDecimal.valueOf(tenureMonths));

        Loans loan = new Loans();
        loan.setUser(user);
        loan.setLoanType(loanType);
        loan.setAmount(amount);
        loan.setTenureMonths(tenureMonths);
        loan.setInterestRate(annualRate);
        loan.setEmi(emi);
        loan.setRemainingAmount(totalPayable);
        loan.setStatus(Loans.LoanStatus.PENDING); // wait for admin

        loansRepo.save(loan);
        return "Loan request submitted. Awaiting admin approval.";
    }

    @Override
    public String approveLoan(Long loanId, Integer adminId) {
        checkAdmin(adminId);

        Loans loan = loansRepo.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if (loan.getStatus() != Loans.LoanStatus.PENDING) {
            return "Loan is not in pending state!";
        }

        loan.setStatus(Loans.LoanStatus.ACTIVE);
        loansRepo.save(loan);

        Accounts account = accountsRepo.findFirstByUser(loan.getUser())
                .orElseThrow(() -> new UserNotFoundException("Account not found for user"));

        recordTransaction(loan, loan.getAmount(), Transactions.TransactionType.LOAN_DISBURSEMENT);

        account.setBalance(account.getBalance().add(loan.getAmount()));
        accountsRepo.save(account);

        return "Loan approved and disbursed. ID: " + loan.getLoan_id();
    }

    @Override
    public String rejectLoan(Long loanId, Integer adminId) {
        checkAdmin(adminId);

        Loans loan = loansRepo.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        loan.setStatus(Loans.LoanStatus.REJECTED);
        loansRepo.save(loan);

        return "Loan request rejected by admin.";
    }

    @Override
    public String payInstallment(Long loanId, BigDecimal paidAmount) {
        Loans loan = loansRepo.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if (loan.getStatus() != Loans.LoanStatus.ACTIVE) {
            return "Loan is not active!";
        }

        BigDecimal emi = loan.getEmi();
        if (paidAmount.compareTo(emi) < 0) {
            BigDecimal shortfall = emi.subtract(paidAmount);
            BigDecimal penalty = shortfall.multiply(BigDecimal.valueOf(0.02));
            loan.setRemainingAmount(loan.getRemainingAmount().add(penalty));
            recordTransaction(loan, paidAmount, Transactions.TransactionType.LOAN_PAYMENT);
            recordTransaction(loan, penalty, Transactions.TransactionType.INTEREST_CHARGE);

            loansRepo.save(loan);
            return "Partial payment done. Penalty added: " + penalty;
        }

        loan.setRemainingAmount(loan.getRemainingAmount().subtract(paidAmount));
        recordTransaction(loan, paidAmount, Transactions.TransactionType.LOAN_PAYMENT);


        if (loan.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            loan.setStatus(Loans.LoanStatus.CLOSED);
        }

        loansRepo.save(loan);
        return "Payment successful. Remaining Loan Balance: " + loan.getRemainingAmount();
    }

    private void checkAdmin(Integer adminId) {
        Users admin = usersRepo.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (admin.getUserRole() != Users.UserRole.ADMIN) {
            throw new SecurityException("Only ADMIN can perform this action");
        }
    }

    private void recordTransaction(Loans loan, BigDecimal amount, Transactions.TransactionType type) {
        Accounts account = accountsRepo.findFirstByUser(loan.getUser())
                .orElseThrow(() -> new UserNotFoundException("Account not found for user"));

        Transactions tx = new Transactions();
        tx.setAccount(account);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        tx.setBeneficiaryAccount("LOAN-" + loan.getLoan_id());
        transactionsRepo.save(tx);
    }


    private BigDecimal getInterestRate(Loans.LoanType loanType) {
        switch (loanType) {
            case PERSONAL: return BigDecimal.valueOf(12);
            case HOME: return BigDecimal.valueOf(7.5);
            case EDUCATION: return BigDecimal.valueOf(5);
            case CAR: return BigDecimal.valueOf(9);
            default: return BigDecimal.valueOf(10);
        }
    }
}
