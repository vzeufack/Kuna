package com.kmercoders.balancedApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmercoders.balancedApp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
   User findByUsername(String username);
}
