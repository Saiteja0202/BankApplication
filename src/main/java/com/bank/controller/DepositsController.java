package com.bank.controller;

import com.bank.model.Deposits;
import com.bank.service.DepositsService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/deposits")
public class DepositsController {

    private final DepositsService depositsService;

    public DepositsController(DepositsService depositsService) {
        this.depositsService = depositsService;
    }

    @PostMapping("/request/{userId}")
    public String requestDeposit(@PathVariable Integer userId, @RequestBody Map<String, String> request) {
        Deposits.DepositType type = Deposits.DepositType.valueOf(request.get("depositType").toUpperCase());
        BigDecimal amount = new BigDecimal(request.get("amount"));
        BigDecimal interestRate = new BigDecimal(request.get("interestRate"));
        LocalDate maturityDate = LocalDate.parse(request.get("maturityDate"));

        return depositsService.requestDeposit(userId, type, amount, interestRate, maturityDate);
    }

    @PutMapping("/approve/admin/{depositId}/{adminId}")
    public String approveDeposit(@PathVariable Long depositId, @PathVariable Integer adminId) {
        return depositsService.approveDeposit(depositId, adminId);
    }

    @PutMapping("/reject/admin/{depositId}/{adminId}")
    public String rejectDeposit(@PathVariable Long depositId, @PathVariable Integer adminId) {
        return depositsService.rejectDeposit(depositId, adminId);
    }

    @PutMapping("/delete-request/user/{depositId}/{userId}")
    public String requestDeleteDeposit(@PathVariable Long depositId, @PathVariable Integer userId) {
        return depositsService.requestDeleteDeposit(depositId, userId);
    }

    @DeleteMapping("/delete/admin/{depositId}/{adminId}")
    public String approveDeleteDeposit(@PathVariable Long depositId, @PathVariable Integer adminId) {
        return depositsService.approveDeleteDeposit(depositId, adminId);
    }

    @GetMapping("/user/{userId}")
    public List<Deposits> getUserDeposits(@PathVariable Integer userId) {
        return depositsService.getUserDeposits(userId);
    }

    @GetMapping("/balance/{depositId}")
    public BigDecimal getDepositBalance(@PathVariable Long depositId) {
        return depositsService.getDepositBalance(depositId);
    }
    
    @GetMapping("/all/{adminId}")
    public List<Deposits> getAllDeposits(@PathVariable Integer adminId) {
        return depositsService.getAllDeposits(adminId);
    }


}
