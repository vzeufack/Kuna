package com.kmercoders.balancedApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmercoders.balancedApp.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
   
}
