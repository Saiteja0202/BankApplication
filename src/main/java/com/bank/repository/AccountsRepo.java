package com.bank.repository;

import com.bank.model.Accounts;
import com.bank.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepo extends JpaRepository<Accounts, Long> {

    List<Accounts> findByUser(Users user);

    boolean existsByAccountNumber(String accountNumber);
    
    Optional<Accounts> findFirstByUser(Users user); 
    


}

