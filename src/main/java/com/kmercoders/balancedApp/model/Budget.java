package com.kmercoders.balancedApp.model;

import java.math.BigDecimal;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "month", "year" }))
public class Budget {
   @Id
   @GeneratedValue
   private Long id;

   private Month month;
   private int year;
   
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "budget")
   @OrderBy("name ASC")
   private Set<Group> groups = new TreeSet<>();

   @NotNull(message = "Please provide an income")
   @Positive
   private BigDecimal income;

   private BigDecimal balance;

   public Budget() {
      this.balance = BigDecimal.ZERO;
   }

   public Budget(Month month, int year, BigDecimal income) {
      this.month = month;
      this.year = year;
      this.income = income;
      this.balance = BigDecimal.ZERO;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public BigDecimal getIncome() {
      return income;
   }

   public void setIncome(BigDecimal income) {
      this.income = income;
   }

   public Month getMonth() {
      return month;
   }

   public void setMonth(Month month) {
      this.month = month;
   }

   public int getYear() {
      return year;
   }

   public void setYear(int year) {
      this.year = year;
   }

   public BigDecimal getBalance() {
      return balance;
   }
   
   public Set<Group> getGroups() {
       return groups;
   }

   public void setGroups(HashSet<Group> groups) {
       this.groups = groups;
   }
}
