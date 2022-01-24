package com.kmercoders.balancedApp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmercoders.balancedApp.model.Transaction;
import com.kmercoders.balancedApp.repositories.TransactionRepository;

@Service
public class TransactionService {
   @Autowired
   private TransactionRepository transactionRepo;
   
   public Transaction save(Transaction transaction) {
      return transactionRepo.save(transaction);
   }
   
   public Optional<Transaction> findById(Long transactionId) {
      return transactionRepo.findById(transactionId);
   }
   
   public void delete(Long transactionId) {
      transactionRepo.deleteById(transactionId);
   }
}
