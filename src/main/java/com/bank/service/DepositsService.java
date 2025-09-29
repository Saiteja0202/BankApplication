
package com.bank.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.bank.model.Deposits;

public interface DepositsService {
    String requestDeposit(Integer userId, Deposits.DepositType depositType, BigDecimal amount,
                          BigDecimal interestRate, LocalDate maturityDate);

    String approveDeposit(Long depositId, Integer adminId);

    String rejectDeposit(Long depositId, Integer adminId);

    String requestDeleteDeposit(Long depositId, Integer userId);

    String approveDeleteDeposit(Long depositId, Integer adminId);

    List<Deposits> getUserDeposits(Integer userId);

    BigDecimal getDepositBalance(Long depositId);
    
    List<Deposits> getAllDeposits(Integer adminId);


}
