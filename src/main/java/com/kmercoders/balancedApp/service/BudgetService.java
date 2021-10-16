package com.kmercoders.balancedApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kmercoders.balancedApp.model.Budget;
import com.kmercoders.balancedApp.repositories.BudgetRepository;

@Service
public class BudgetService {
	@Autowired
	private BudgetRepository budgetRepo;
	
	public Budget save(Budget budget) {
		return budgetRepo.save(budget);	
	}
	
	public Optional<Budget> findById(Long budgetId) {
   	return budgetRepo.findById(budgetId);
   }

	public List<Budget> findAll() {
		return budgetRepo.findAll();
	}
	
	public void delete(Long budgetId) {
		budgetRepo.deleteById(budgetId);
  }
}
