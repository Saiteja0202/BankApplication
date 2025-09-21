package com.bank.repository;

import com.bank.model.Deposits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepositsRepo extends JpaRepository<Deposits, Long> {

    @Query("SELECT d FROM Deposits d WHERE d.user.user_id = :userId")
    List<Deposits> findByUserId(@Param("userId") Integer userId);
}
