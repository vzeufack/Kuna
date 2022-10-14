package com.kmercoders.balancedApp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
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

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "month", "year" }))
public class Budget {
   @Id
   @GeneratedValue
   private Long id;

   private Month month;
   private int year;
   
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "budget")
   @OrderBy("id ASC")
   private Set<Group> groups = new TreeSet<>();
   
   public Budget() {}

   public Budget(Month month, int year, BigDecimal income) {
      this.month = month;
      this.year = year;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
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
   
   public Set<Group> getGroups() {
       return groups;
   }

   public void setGroups(TreeSet<Group> groups) {
       this.groups = groups;
   }
   
   public BigDecimal getTotalIncome() {
      BigDecimal totalIncome = BigDecimal.ZERO;
      Set<Group> groups = getGroups();
      
      for(Group group: groups) {
         if(group.getName().equalsIgnoreCase("income")) {
            totalIncome = group.getTotalAllocation();
         }  
      }
      
      return totalIncome;
   }
   
   public BigDecimal getTotalPlanned() {
      BigDecimal totalPlanned = BigDecimal.ZERO;
      Set<Group> groups = getGroups();
      
      for(Group group: groups) {
         if(!group.getName().equalsIgnoreCase("income"))
            totalPlanned = totalPlanned.add(group.getTotalAllocation());
      }
      
      return totalPlanned;
   }
   
   public BigDecimal getTotalUnplanned() {
      return getTotalIncome().subtract(getTotalPlanned());
   }
   
   
   public BigDecimal getTotalSpent() {
      BigDecimal totalSpent = BigDecimal.ZERO;
      Set<Group> groups = getGroups();
      
      for(Group group: groups) {
         if(!group.getName().equalsIgnoreCase("income"))
            totalSpent = totalSpent.add(group.getTotalSpent());
      }
      
      return totalSpent; 
   }
   
   public BigDecimal getTotalRemaining() {
      return getTotalIncome().subtract(getTotalSpent());
   }
   
   public double getPercentSpent() {
      return getTotalSpent().multiply(new BigDecimal(100)).divide(getTotalIncome(), 10, RoundingMode.HALF_UP).doubleValue();   
   }
}
