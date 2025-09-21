package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.TransactionNotFoundException;
import com.bank.model.Transactions;
import com.bank.repository.AccountsRepo;
import com.bank.repository.TransactionsRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepo transactionsRepo;
    private final AccountsRepo accountsRepo;

    public TransactionsServiceImpl(TransactionsRepo transactionsRepo, AccountsRepo accountsRepo) {
        this.transactionsRepo = transactionsRepo;
        this.accountsRepo = accountsRepo;
    }

    @Override
    public List<Transactions> getTransactionsByAccount(Long accountId) {
        accountsRepo.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));

        return transactionsRepo.findByAccountIdOrderByTimestampDesc(accountId);
    }

    @Override
    public Transactions getTransactionById(Long id) {
        return transactionsRepo.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with ID " + id + " not found"));
    }
}
