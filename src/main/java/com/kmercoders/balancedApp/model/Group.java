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
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "grp")
public class Group implements Comparable<Group> {
   @Id
   @GeneratedValue
   private Long id;
   
   @NotBlank(message = "Please provide a group name")
   private String name;
   
   @ManyToOne
   private Budget budget;
   
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
   @OrderBy("name ASC")
   private Set<Category> categories = new TreeSet<>();
   
   public Group() {}
   
   public Group(String name) {
      this.name = name;
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
   
   public Budget getBudget() {
      return budget;
   }

   public void setBudget(Budget budget) {
      this.budget = budget;
   }

   public Set<Category> getCategories() {
      return categories;
   }

   public void setCategories(Set<Category> categories) {
      this.categories = categories;
   }

   @Override
   public int compareTo(Group o) {
      if (this.getName() != null && o.getName() != null)
         return this.getName().compareTo(o.getName());
      
      return 0;
   } 
}
