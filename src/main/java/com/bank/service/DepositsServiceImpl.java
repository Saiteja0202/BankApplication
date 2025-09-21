package com.bank.service;

import com.bank.exception.DepositNotFoundException;
import com.bank.exception.UserNotFoundException;
import com.bank.model.*;
import com.bank.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class DepositsServiceImpl implements DepositsService {

    private final DepositsRepo depositsRepo;
    private final UsersRepo usersRepo;
    private final AccountsRepo accountsRepo;
    private final TransactionsRepo transactionsRepo;

    public DepositsServiceImpl(DepositsRepo depositsRepo, UsersRepo usersRepo,
                               AccountsRepo accountsRepo, TransactionsRepo transactionsRepo) {
        this.depositsRepo = depositsRepo;
        this.usersRepo = usersRepo;
        this.accountsRepo = accountsRepo;
        this.transactionsRepo = transactionsRepo;
    }

    @Override
    public String requestDeposit(Integer userId, Deposits.DepositType depositType, BigDecimal amount,
                                 BigDecimal interestRate, LocalDate maturityDate) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Deposits deposit = new Deposits();
        deposit.setUser(user);
        deposit.setDepositType(depositType);
        deposit.setAmount(amount);
        deposit.setInterestRate(interestRate);
        deposit.setMaturityDate(maturityDate);
        deposit.setStatus(Deposits.DepositStatus.PENDING);
        deposit.setRequestStatus(Deposits.RequestStatus.PENDING);


        depositsRepo.save(deposit);

        return "Deposit request submitted. Awaiting admin approval.";
    }

    @Override
    public String approveDeposit(Long depositId, Integer adminId) {
        checkAdmin(adminId);

        Deposits deposit = getDeposit(depositId);
        if (deposit.getRequestStatus() != Deposits.RequestStatus.PENDING) {
            return "Deposit is not in pending state!";
        }

        deposit.setStatus(Deposits.DepositStatus.ACTIVE);
        deposit.setRequestStatus(Deposits.RequestStatus.APPROVED);
        depositsRepo.save(deposit);

        Accounts account = accountsRepo.findFirstByUser(deposit.getUser())
                .orElseThrow(() -> new UserNotFoundException("Account not found for user"));

        recordTransaction(account, deposit.getAmount(),
                mapDepositTypeToTransactionType(deposit.getDepositType()), "DEPOSIT-" + deposit.getDeposit_id());

        return "Deposit approved and activated. ID: " + deposit.getDeposit_id();
    }

    @Override
    public String rejectDeposit(Long depositId, Integer adminId) {
        checkAdmin(adminId);

        Deposits deposit = getDeposit(depositId);
        deposit.setStatus(Deposits.DepositStatus.PENDING);
        deposit.setRequestStatus(Deposits.RequestStatus.NOTAPPLIED);
        depositsRepo.save(deposit);

        return "Deposit request rejected. Customer may reapply.";
    }

    @Override
    public String requestDeleteDeposit(Long depositId, Integer userId) {
        Deposits deposit = getDeposit(depositId);
        if (deposit.getUser().getUser_id() != userId) {
            throw new DepositNotFoundException("Deposit does not belong to this user");
        }


        deposit.setRequestStatus(Deposits.RequestStatus.PENDING);
        depositsRepo.save(deposit);

        return "Delete request submitted. Awaiting admin approval.";
    }

    @Override
    public String approveDeleteDeposit(Long depositId, Integer adminId) {
        checkAdmin(adminId);

        Deposits deposit = getDeposit(depositId);
        Accounts account = accountsRepo.findFirstByUser(deposit.getUser())
                .orElseThrow(() -> new UserNotFoundException("Account not found for user"));

        recordTransaction(account, deposit.getAmount(),
                Transactions.TransactionType.DEPOSIT, "DEPOSIT-DELETE-" + deposit.getDeposit_id());

        depositsRepo.delete(deposit);

        return "Deposit deleted after admin approval.";
    }

    @Override
    public List<Deposits> getUserDeposits(Integer userId) {
        return depositsRepo.findByUserId(userId);
    }

    @Override
    public BigDecimal getDepositBalance(Long depositId) {
        Deposits deposit = getDeposit(depositId);
        return deposit.getAmount();
    }

    private Deposits getDeposit(Long depositId) {
        return depositsRepo.findById(depositId)
                .orElseThrow(() -> new DepositNotFoundException("Deposit not found"));
    }

    private void checkAdmin(Integer adminId) {
        Users admin = usersRepo.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (admin.getUserRole() != Users.UserRole.ADMIN) {
            throw new SecurityException("Only ADMIN can perform this action");
        }
    }

    private void recordTransaction(Accounts account, BigDecimal amount,
                                   Transactions.TransactionType type, String beneficiaryAccount) {
        Transactions tx = new Transactions();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        tx.setType(type);
        tx.setBeneficiaryAccount(beneficiaryAccount);
        transactionsRepo.save(tx);
    }

    private Transactions.TransactionType mapDepositTypeToTransactionType(Deposits.DepositType depositType) {
        switch (depositType) {
            case FIXED: return Transactions.TransactionType.FIXED_DEPOSIT;
            case RECURRING: return Transactions.TransactionType.RECURRING_DEPOSIT;
            case SAVINGS: return Transactions.TransactionType.DEPOSIT;
            default: return Transactions.TransactionType.DEPOSIT;
        }
    }
}
