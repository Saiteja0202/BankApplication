package com.bank.repository;

import com.bank.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionsRepo extends JpaRepository<Transactions, Long> {
    
	@Query("SELECT t FROM Transactions t WHERE t.account.id = :accountId ORDER BY t.timestamp DESC")
	List<Transactions> findByAccountIdOrderByTimestampDesc(@Param("accountId") Long accountId);

	
}
