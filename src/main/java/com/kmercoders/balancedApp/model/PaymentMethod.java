package com.kmercoders.balancedApp.model;

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
public class PaymentMethod {
   @Id
   @GeneratedValue
   private long id;
   
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
   
   public long getId() {
      return id;
   }
   
   public void setId(long id) {
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
}
