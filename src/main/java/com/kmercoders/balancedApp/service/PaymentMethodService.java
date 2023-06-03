package com.kmercoders.balancedApp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.kmercoders.balancedApp.model.PaymentMethod;
import com.kmercoders.balancedApp.model.Transaction;
import com.kmercoders.balancedApp.model.User;
import com.kmercoders.balancedApp.repositories.PaymentMethodRepository;

@Service
public class PaymentMethodService {
   @Autowired
   private PaymentMethodRepository paymentRepo;

   public PaymentMethod save(User user, PaymentMethod paymentMethod) {
      paymentMethod.setUser(user);
      return paymentRepo.save(paymentMethod);
   }
   
   public TreeSet<PaymentMethod> getPaymentMethods(@AuthenticationPrincipal User user){      
      return paymentRepo.findByUser(user);
   }
   
   public TreeSet<PaymentMethod> getPaymentMethods(@AuthenticationPrincipal User user, Long budgetId){      
	   TreeSet<PaymentMethod> paymentMethods = paymentRepo.findByUser(user);
	   for(PaymentMethod paymentMethod: paymentMethods) {
		   Set<Transaction> transactions = paymentMethod.getTransactions();
		   Set<Transaction> transactionsToRemove = new TreeSet<>();
		   for (Transaction transaction: transactions) {
			   if(transaction.getCategory().getGroup().getBudget().getId() != budgetId)
				   transactionsToRemove.add(transaction);
		   }
		   transactions.removeAll(transactionsToRemove);
	   }
	   return paymentMethods;
   }

   public Optional<PaymentMethod> findById(Long paymentId) {
      return paymentRepo.findById(paymentId);
   }

   public List<PaymentMethod> findAll() {
      return paymentRepo.findAll();
   }

   public void delete(Long paymentId) {
      paymentRepo.deleteById(paymentId);
   }
}
