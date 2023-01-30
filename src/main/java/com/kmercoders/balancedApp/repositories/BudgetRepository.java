package com.kmercoders.balancedApp.repositories;


import java.util.TreeSet;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmercoders.balancedApp.model.Budget;
import com.kmercoders.balancedApp.model.User;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
   TreeSet<Budget> findByUser(User user);
}
