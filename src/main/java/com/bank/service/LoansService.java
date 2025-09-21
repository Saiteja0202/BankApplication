package com.bank.service;

import com.bank.model.Loans;
import java.math.BigDecimal;

public interface LoansService {
    String requestLoan(Integer userId, Loans.LoanType loanType, BigDecimal amount, Integer tenureMonths);
    String approveLoan(Long loanId, Integer adminId);
    String rejectLoan(Long loanId, Integer adminId);
    String payInstallment(Long loanId, BigDecimal paidAmount);
}
