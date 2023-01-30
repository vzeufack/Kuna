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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "month", "year", "user_id" }))
public class Budget implements Comparable<Budget>{
   @Id
   @GeneratedValue
   private Long id;

   private Month month;
   private int year;
   
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "budget")
   @OrderBy("id ASC")
   private Set<Group> groups = new TreeSet<>();
   
   //@ManyToMany
   //@JoinTable(name = "user_budget", inverseJoinColumns = @JoinColumn(name = "budget_id"), joinColumns = @JoinColumn(name = "user_id"))
   //@JsonIgnore
   //private Set <User> users = new TreeSet<>();
   
   @ManyToOne
   private User user;
   
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
   
   /*public Set<User> getUsers() {
      return users;
   }

   public void setUsers(Set<User> users) {
      this.users = users;
   }*/
   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
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
      if(getTotalIncome().equals(BigDecimal.ZERO))
         return 0.0;
      else
         return getTotalSpent().multiply(new BigDecimal(100)).divide(getTotalIncome(), 10, RoundingMode.HALF_UP).doubleValue();   
   }
   
   @Override
   public int compareTo(Budget budget) {
       return this.id.compareTo(budget.getId());
   }
}
