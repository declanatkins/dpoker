package com.dpoker.poker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dpoker.poker.model.User;

public interface UserRespository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
