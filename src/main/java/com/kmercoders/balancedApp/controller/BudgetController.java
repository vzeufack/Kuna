package com.kmercoders.balancedApp.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kmercoders.balancedApp.model.Budget;
import com.kmercoders.balancedApp.model.Category;
import com.kmercoders.balancedApp.model.Group;
import com.kmercoders.balancedApp.model.Transaction;
import com.kmercoders.balancedApp.service.BudgetService;

@Controller
@RequestMapping(value = { "/budget", "/" })
public class BudgetController {
   @Autowired
   private BudgetService budgetService;

   @GetMapping(value = { "list", "" })
   public String showBudgets(ModelMap model) {
      List<Budget> budgets = budgetService.findAll();

      model.addAttribute("budgets", budgets);
      return "budget/list";
   }

   @GetMapping(value = "create")
   public String showBudgetCreationForm(ModelMap model) {
      Budget budget = new Budget();
      model.addAttribute("budget", budget);
      model.addAttribute("months", Month.values());
      model.addAttribute("currentMonth", LocalDate.now().getMonth());
      model.addAttribute("currentYear", LocalDate.now().getYear());
      return "budget/create";
   }

   @PostMapping(value = "create")
   public String createBudget(ModelMap model, @ModelAttribute("budget") @Valid Budget budget, BindingResult result) {
      if (result.hasErrors()) {
         feedModel(model, budget);
         return "budget/create";
      }

      try {
         budgetService.save(budget);
      } catch (Exception e) {
         feedModel(model, budget);
         model.addAttribute("isDuplicate", true);
         return "budget/create";
      }
      return "redirect:/budget/list";
   }

   @GetMapping(value = "view/{budgetId}")
   public String showBudget(ModelMap model, @PathVariable Long budgetId) {
      Budget budget = budgetService.findById(budgetId).get();
      
      BigDecimal totalSpent = BigDecimal.ZERO;
      for(Group group: budget.getGroups()) {
         for(Category category: group.getCategories()) {
            for(Transaction transaction: category.getTransactions()) {
               totalSpent = totalSpent.add(transaction.getAmount());
            }
         }
      }
      
      model.put("budget", budget);
      model.addAttribute("totalSpent", totalSpent);
      model.addAttribute("remaining", budget.getIncome().subtract(totalSpent));

      return "budget/view";
   }

   @GetMapping(value = "edit/{budgetId}")
   public String showEditBudgetForm(ModelMap model, @PathVariable Long budgetId) {
      Budget budget = budgetService.findById(budgetId).get();

      feedModel(model, budget);

      return "budget/edit";
   }

   @PostMapping(value = "edit/{budgetId}")
   public String updateBudget(ModelMap model, @ModelAttribute("budget") @Valid Budget budget, BindingResult result,
         @PathVariable Long budgetId) {
      Budget budgetFromDB = budgetService.findById(budgetId).get();

      if (result.hasErrors()) {
         budget.setId(budgetId);
         feedModel(model, budget);
         return "budget/edit";
      }

      budgetFromDB.setIncome(budget.getIncome());
      budgetFromDB.setMonth(budget.getMonth());
      budgetFromDB.setYear(budget.getYear());

      try {
         budgetService.save(budgetFromDB);
      } catch (Exception e) {
         budget.setId(budgetId);
         feedModel(model, budget);
         model.addAttribute("isDuplicate", true);
         return "budget/edit";
      }
      return "redirect:/budget/list";
   }

   @RequestMapping("delete/{budgetId}")
   public String deleteBudget(@PathVariable Long budgetId) {
      budgetService.delete(budgetId);
      return "redirect:/budget/list";
   }

   public void feedModel(ModelMap model, Budget budget) {
      model.put("budget", budget);
      model.addAttribute("months", Month.values());
      model.addAttribute("currentMonth", budget.getMonth());
      model.addAttribute("currentYear", budget.getYear());
   }
}
