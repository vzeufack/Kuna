package com.kmercoders.balancedApp.model;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class PaymentMethod implements Comparable<PaymentMethod> {
   @Id
   @GeneratedValue
   private Long id;
   
   private String name;
   private PaymentType paymentType;
   
   @ManyToOne
   private User user;
   
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "paymentMethod")
   @OrderBy("date DESC, dateCreated DESC")
   private Set<Transaction> transactions = new TreeSet<>();
   
   public PaymentMethod() {}
   
   public PaymentMethod(String name, PaymentType paymentType) {
      super();
      this.name = name;
      this.paymentType = paymentType;
   }
   
   public String getName() {
      return name;
   }
   
   public void setName(String name) {
      this.name = name;
   }
   
   public PaymentType getPaymentType() {
      return paymentType;
   }
   
   public void setPaymentType(PaymentType paymentType) {
      this.paymentType = paymentType;
   }
   
   public Long getId() {
      return id;
   }
   
   public void setId(Long id) {
      this.id = id;
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public Set<Transaction> getTransactions() {
      return transactions;
   }

   public void setTransactions(Set<Transaction> transactions) {
      this.transactions = transactions;
   }
   
   public BigDecimal getTotal(Long budgetId) {
	   BigDecimal result = new BigDecimal(0);
	   for(Transaction t: transactions) {
		   if(t.getCategory().getGroup().getBudget().getId() == budgetId)
			   result = result.add(t.getAmount());
	   }
	   return result;
   }
   
   public BigDecimal getTotal(Long budgetId, boolean isSettled) {
	   BigDecimal result = new BigDecimal(0);
	   for(Transaction t: transactions) {
		   if(t.getCategory().getGroup().getBudget().getId() == budgetId && t.getIsSettled() == isSettled)
			   result = result.add(t.getAmount());
	   }
	   return result;
   }
   
   @Override
   public int compareTo(PaymentMethod method) {
       return this.id.compareTo(method.getId());
   }
}
