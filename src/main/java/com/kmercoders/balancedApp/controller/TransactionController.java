package com.kmercoders.balancedApp.controller;

import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kmercoders.balancedApp.model.Budget;
import com.kmercoders.balancedApp.model.Category;
import com.kmercoders.balancedApp.model.Group;
import com.kmercoders.balancedApp.model.Transaction;
import com.kmercoders.balancedApp.model.User;
import com.kmercoders.balancedApp.response.GroupResponse;
import com.kmercoders.balancedApp.service.BudgetService;
import com.kmercoders.balancedApp.service.CategoryService;
import com.kmercoders.balancedApp.service.GroupService;
import com.kmercoders.balancedApp.service.PaymentMethodService;
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
   
   @Autowired
   private PaymentMethodService paymentMethodService;
   
   @PostMapping(value = "create")
   @ResponseBody
   public GroupResponse createTransaction(@ModelAttribute @Valid Transaction transaction, BindingResult result, @PathVariable Long budgetId, @PathVariable Long groupId, @PathVariable Long categoryId) {	   
	   GroupResponse response = new GroupResponse();
	   
       if (result.hasErrors()) {
    	  Map<String, String> errors = result.getFieldErrors().stream()
              .collect( Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

          response.setValidated(false);
          response.setErrorMessages(errors);
       } else {
           response.setValidated(true);
           Category category = categoryService.findById(categoryId).get();
           transaction.setCategory(category);
           transactionService.save(transaction);
       }
       
       return response;
   } 
   
   @PostMapping(value = "edit/{transactionId}")
   @ResponseBody
   public GroupResponse updateCategory(@ModelAttribute @Valid Transaction transaction, BindingResult result,
	         						   @PathVariable Long transactionId, @PathVariable Long categoryId, @PathVariable Long groupId, @PathVariable Long budgetId) {	   
	   GroupResponse response = new GroupResponse();
	   
       if (result.hasErrors()) {
    	  Map<String, String> errors = result.getFieldErrors().stream()
              .collect( Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

           response.setValidated(false);
           response.setErrorMessages(errors);
       } else {
           response.setValidated(true);
           Transaction transactionFromDB = transactionService.findById(transactionId).get();
           transactionFromDB.setDate(transaction.getDate());
           transactionFromDB.setAmount(transaction.getAmount());
           transactionFromDB.setNote(transaction.getNote());           
           transactionFromDB.setPaymentMethod(transaction.getPaymentMethod());
           transactionFromDB.setIsSettled(transaction.getIsSettled());
           
           transactionService.save(transactionFromDB);
       }
       
       return response;
   }
   
   @GetMapping(value = "edit/{transactionId}/pm")
   public String showEditTransactionFormPaymentMethod(@AuthenticationPrincipal User user, ModelMap model, @PathVariable Long transactionId, @PathVariable Long categoryId, @PathVariable Long groupId, @PathVariable Long budgetId) {
      Transaction transaction = transactionService.findById(transactionId).get();
      Category category = categoryService.findById(categoryId).get();
      Budget budget = budgetService.findById(budgetId).get();
      Group group = groupService.findById(groupId).get();
      
      model.addAttribute("transaction", transaction);
      model.addAttribute("category", category);
      model.addAttribute("budget", budget);
      model.addAttribute("group", group);
      model.addAttribute("paymentMethods", paymentMethodService.getPaymentMethods(user));
      return "transaction/edit_pm";
   }
   
   @PostMapping(value = "edit/{transactionId}/pm")
   public String updateTransactionPaymentMethod(@AuthenticationPrincipal User user, ModelMap model, @ModelAttribute("transaction") @Valid Transaction transaction, BindingResult result,
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
         model.addAttribute("paymentMethods", paymentMethodService.getPaymentMethods(user));
         return "transaction/edit_pm";
      }
      
      transactionFromDB.setDate(transaction.getDate());
      transactionFromDB.setAmount(transaction.getAmount());
      transactionFromDB.setNote(transaction.getNote());
      transactionFromDB.setPaymentMethod(transaction.getPaymentMethod());
      transactionFromDB.setIsSettled(transaction.getIsSettled());
      
      transactionService.save(transactionFromDB);
      
      return "redirect:/budget/transactions_by_payment_method/" + budgetId;
   }
   
   @RequestMapping("delete/{transactionId}/{pageId}")
   public String deleteTransaction(@PathVariable Long transactionId, @PathVariable Long budgetId, @PathVariable byte pageId) {
      transactionService.delete(transactionId);
      return pageId == 0 ? "redirect:/budget/view/" + budgetId : "redirect:/budget/transactions_by_payment_method/" + budgetId;
   }
   
   @PostMapping(value = "delete/{transactionId}")
   public ResponseEntity<?> deleteCategory(@PathVariable Long transactionId) {	   
	   try {
		   transactionService.delete(transactionId);
	    } catch (Exception e) {
	  	  e.printStackTrace();
	  	  return ResponseEntity.badRequest().body(transactionId);
	    }
		   
	    return ResponseEntity.ok(transactionId);
   }
}
