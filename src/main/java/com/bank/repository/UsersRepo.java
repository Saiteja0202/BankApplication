package com.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.model.Users;



@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {

    Optional<Users> findByUserToken(String token);

    boolean existsByName(String name);

    Optional<Users> findByName(String name);


    Optional<Users> findByEmail(String email);
}

