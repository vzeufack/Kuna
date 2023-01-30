package com.kmercoders.balancedApp.service;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import com.kmercoders.balancedApp.model.Budget;
import com.kmercoders.balancedApp.model.User;
import com.kmercoders.balancedApp.repositories.BudgetRepository;

@Service
public class BudgetService {
   @Autowired
   private BudgetRepository budgetRepo;

   public Budget save(User user, Budget budget) {
      budget.setUser(user);
      return budgetRepo.save(budget);
   }
   
   public TreeSet<Budget> getBudgets(@AuthenticationPrincipal User user){      
      return budgetRepo.findByUser(user);
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
   
   public Budget getLastBudget(User user) {
      TreeSet<Budget> budgets = budgetRepo.findByUser(user);
      Long maxId = 0L;
      Budget lastBudget = null;
      
      for(Budget budget: budgets) {
         if(budget.getId() > maxId) {
            maxId = budget.getId();
            lastBudget = budget;
         }
      }
      
      return lastBudget;
   }
}
