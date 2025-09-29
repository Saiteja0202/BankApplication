package com.bank.service;

import com.bank.model.Loans;
import com.bank.model.Users;

import java.math.BigDecimal;
import java.util.List;

public interface LoansService {
    String requestLoan(Integer userId, Loans.LoanType loanType, BigDecimal amount, Integer tenureMonths);
    String approveLoan(Long loanId, Integer adminId);
    String rejectLoan(Long loanId, Integer adminId);
    String payInstallment(Long loanId, BigDecimal paidAmount);
    
    List<Loans> getUserLoans(Integer userId);

    
    List<Loans> getAllLoans(Integer adminId);
}

