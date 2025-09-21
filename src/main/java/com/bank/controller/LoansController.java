package com.bank.controller;

import com.bank.model.Loans;
import com.bank.service.LoansService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
@RestController
@RequestMapping("/loans")
public class LoansController {

    private final LoansService loansService;

    public LoansController(LoansService loansService) {
        this.loansService = loansService;
    }

    @PostMapping("/request/{userId}")
    public String requestLoan(@PathVariable Integer userId,
                              @RequestBody Map<String, Object> request) {
        Loans.LoanType type = Loans.LoanType.valueOf(request.get("loanType").toString().toUpperCase());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        Integer tenure = Integer.parseInt(request.get("tenureMonths").toString());

        return loansService.requestLoan(userId, type, amount, tenure);
    }

    @PostMapping("/approve/{loanId}/{adminId}")
    public String approveLoan(@PathVariable Long loanId, @PathVariable Integer adminId) {
        return loansService.approveLoan(loanId, adminId);
    }

    @PostMapping("/reject/{loanId}/{adminId}")
    public String rejectLoan(@PathVariable Long loanId, @PathVariable Integer adminId) {
        return loansService.rejectLoan(loanId, adminId);
    }

    @PostMapping("/pay/{loanId}")
    public String payInstallment(@PathVariable Long loanId,
                                 @RequestBody Map<String, Object> request) {
        BigDecimal paidAmount = new BigDecimal(request.get("amount").toString());
        return loansService.payInstallment(loanId, paidAmount);
    }
}
