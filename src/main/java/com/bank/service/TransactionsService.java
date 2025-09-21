package com.bank.service;

import com.bank.model.Transactions;
import java.util.List;

public interface TransactionsService {
    List<Transactions> getTransactionsByAccount(Long accountId);
    Transactions getTransactionById(Long id);
}

