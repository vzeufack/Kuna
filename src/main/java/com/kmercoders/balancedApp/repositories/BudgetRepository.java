package com.kmercoders.balancedApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmercoders.balancedApp.model.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

}
