package com.kmercoders.balancedApp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
public class Transaction implements Comparable<Transaction> {
   @Id @GeneratedValue
   private Long id;
   
   @NotNull(message = "Please provide the date")
   private LocalDate date;
   
   private LocalDateTime dateCreated;
   
   @NotNull(message = "Please provide the transaction amount")
   @Positive
   private BigDecimal amount;
   private String note;
   
   @ManyToOne
   private Category category;
   
   @ManyToOne
   private Category paymentMethod;
   
   public Transaction() {
      date = LocalDate.now();
      dateCreated = LocalDateTime.now();
   }   
   
   public Long getId() {
      return id;
   }
   
   public void setId(Long id) {
      this.id = id;
   }
   
   public LocalDate getDate() {
      return date;
   }
   
   public void setDate(LocalDate date) {
      this.date = date;
   }
   
   public BigDecimal getAmount() {
      return amount;
   }
   
   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }
   public String getNote() {
      return note;
   }
   
   public void setNote(String note) {
      this.note = note;
   }

   public LocalDateTime getDateCreated() {
      return dateCreated;
   }

   public void setDateCreated(LocalDateTime dateCreated) {
      this.dateCreated = dateCreated;
   }

   public Category getCategory() {
      return category;
   }

   public void setCategory(Category category) {
      this.category = category;
   }

   public Category getPaymentMethod() {
      return paymentMethod;
   }

   public void setPaymentMethod(Category paymentMethod) {
      this.paymentMethod = paymentMethod;
   }

   @Override
   public int compareTo(Transaction o) {
      int dateComparisonResult = this.getDate().compareTo(o.getDate());
      if(dateComparisonResult == 0)
         return this.getDateCreated().compareTo(o.getDateCreated());
      else
         return dateComparisonResult;
   }
}
