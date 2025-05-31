package com.dpoker.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dpoker.web.model.User;

public interface UserRespository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
