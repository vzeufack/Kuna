package com.kmercoders.balancedApp.controller;

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
import com.kmercoders.balancedApp.service.CategoryService;
import com.kmercoders.balancedApp.service.GroupService;
import com.kmercoders.balancedApp.service.TransactionService;

@Controller
@RequestMapping(value = { "/budget/{budgetId}/group/{groupId}/category/{categoryId}/transaction"})
public class TransactionController {
   @Autowired
   private TransactionService transactionService;
   
   @Autowired
   private CategoryService categoryService;
   
   @Autowired
   private GroupService groupService;
   
   @Autowired
   private BudgetService budgetService;
   
   @GetMapping(value = "create")
   public String showTransactionCreationForm(ModelMap model, @PathVariable Long budgetId, @PathVariable Long groupId, @PathVariable Long categoryId) {
      Transaction transaction = new Transaction();
      Budget budget = budgetService.findById(budgetId).get();
      Group group = groupService.findById(groupId).get();
      Category category = categoryService.findById(categoryId).get();
      
      model.addAttribute("transaction", transaction);
      model.addAttribute("category", category);
      model.addAttribute("budget", budget);
      model.addAttribute("group", group);
      return "transaction/create";
   }
   
   @PostMapping(value = "create")
   public String createTransaction(ModelMap model, @ModelAttribute("transaction") @Valid Transaction transaction, BindingResult result, @PathVariable Long budgetId, @PathVariable Long groupId, @PathVariable Long categoryId) {
      Budget budget = budgetService.findById(budgetId).get();
      Group group = groupService.findById(groupId).get();
      Category category = categoryService.findById(categoryId).get();
      
      if (result.hasErrors()) {
         model.addAttribute("transaction", transaction);
         model.addAttribute("category", category);
         model.addAttribute("budget", budget);
         model.addAttribute("group", group);
         return "transaction/create";
      }
      
      transaction.setCategory(category);
      transactionService.save(transaction);
      return "redirect:/budget/view/" + budgetId;
   }
   
   @GetMapping(value = "edit/{transactionId}")
   public String showEditTransactionForm(ModelMap model, @PathVariable Long transactionId, @PathVariable Long categoryId, @PathVariable Long groupId, @PathVariable Long budgetId) {
      Transaction transaction = transactionService.findById(transactionId).get();
      Category category = categoryService.findById(categoryId).get();
      Budget budget = budgetService.findById(budgetId).get();
      Group group = groupService.findById(groupId).get();
      
      model.addAttribute("transaction", transaction);
      model.addAttribute("category", category);
      model.addAttribute("budget", budget);
      model.addAttribute("group", group);
      return "transaction/edit";
   }
   
   @PostMapping(value = "edit/{transactionId}")
   public String updateTransaction(ModelMap model, @ModelAttribute("transaction") @Valid Transaction transaction, BindingResult result,
         @PathVariable Long transactionId, @PathVariable Long categoryId, @PathVariable Long groupId, @PathVariable Long budgetId) {
      
      Transaction transactionFromDB = transactionService.findById(transactionId).get();
      Category category = categoryService.findById(categoryId).get();
      Budget budget = budgetService.findById(budgetId).get();
      Group group = groupService.findById(groupId).get();

      if (result.hasErrors()) {
         transaction.setId(transactionId);
         model.addAttribute("transaction", transaction);
         model.addAttribute("category", category);
         model.addAttribute("budget", budget);
         model.addAttribute("group", group);
         return "transaction/edit";
      }
      
      transactionFromDB.setDate(transaction.getDate());
      transactionFromDB.setAmount(transaction.getAmount());
      transactionFromDB.setNote(transaction.getNote());
      
      transactionService.save(transactionFromDB);
      
      return "redirect:/budget/view/" + budgetId;
   }
   
   @RequestMapping("delete/{transactionId}")
   public String deleteTransaction(@PathVariable Long transactionId, @PathVariable Long budgetId) {
      transactionService.delete(transactionId);
      return "redirect:/budget/view/" + budgetId;
   }
}
