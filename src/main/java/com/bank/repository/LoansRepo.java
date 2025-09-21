package com.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.model.Loans;

public interface LoansRepo extends JpaRepository<Loans, Long> {
	
	
	
}
