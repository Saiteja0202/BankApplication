package com.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.model.Loans;
import com.bank.model.Users;

public interface LoansRepo extends JpaRepository<Loans, Long> {
	
	List<Loans> findByUser(Users user);

	
}
