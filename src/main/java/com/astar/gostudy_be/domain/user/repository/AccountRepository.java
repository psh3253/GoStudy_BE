package com.astar.gostudy_be.domain.user.repository;

import com.astar.gostudy_be.domain.user.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);
}
