package com.kmercoders.balancedApp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Category implements Comparable<Category> {   
   @Id
   @GeneratedValue
   private Long id;
   
   @NotBlank(message = "Please provide a category name")
   private String name;
   
   @NotNull(message = "Please provide an allocation for this category")
   @Min(value = 0)
   private BigDecimal allocation;
   
   @ManyToOne
   private Group group;
   
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category")
   @OrderBy("date DESC, dateCreated DESC")
   private Set<Transaction> transactions = new TreeSet<>();
   
   public Category() {}
   
   public Category(String name) {
      this.name = name;
      this.allocation = BigDecimal.ZERO;
   }
   
   public Category(String name, BigDecimal allocation) {
      this.name = name;
      this.allocation = allocation;
   }
   
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public BigDecimal getAllocation() {
      return allocation;
   }

   public void setAllocation(BigDecimal allocation) {
      this.allocation = allocation;
   }

   public Group getGroup() {
      return group;
   }

   public void setGroup(Group group) {
      this.group = group;
   }   

   public Set<Transaction> getTransactions() {
      return transactions;
   }

   public void setTransactions(Set<Transaction> transactions) {
      this.transactions = transactions;
   }

   @Override
   public int compareTo(Category o) {
      //if (this.getName() != null && o.getName() != null)
         return this.getName().compareTo(o.getName());
      
      //return 0;
   }  
   
   public BigDecimal getTotalSpent() {
      Set<Transaction> txns = getTransactions();
      BigDecimal total = BigDecimal.ZERO;
      for(Transaction txn: txns) {
         total = total.add(txn.getAmount());
      }
      return total;
   }
   
   public BigDecimal getTotalLeft() {
      return getAllocation().subtract(getTotalSpent());
   }
   
   public double getPercentSpent() {
	   if(getAllocation().equals(BigDecimal.ZERO))
         return 0.0;
      else
         return getTotalSpent().multiply(new BigDecimal(100)).divide(getAllocation(), 10, RoundingMode.HALF_UP).doubleValue();  
   }
   
   public double getPercentLeft() {
	   if(getAllocation().equals(BigDecimal.ZERO))
         return 0.0;
      else
         return getTotalLeft().multiply(new BigDecimal(100)).divide(getAllocation(), 10, RoundingMode.HALF_UP).doubleValue();  
   }
   
   public BigDecimal getRemaining() {
      return getAllocation().subtract(getTotalSpent());
   }
   
}
