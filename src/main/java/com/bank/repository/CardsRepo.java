package com.bank.repository;

import com.bank.model.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardsRepo extends JpaRepository<Cards, Long> {
    Optional<Cards> findByCardNumber(String cardNumber);

    @Query("SELECT c FROM Cards c WHERE c.user.user_id = :userId")
    List<Cards> findByUserId(@Param("userId") Integer userId);
}
