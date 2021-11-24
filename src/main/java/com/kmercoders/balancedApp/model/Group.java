package com.kmercoders.balancedApp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "grp")
public class Group {
   @Id
   @GeneratedValue
   private Long id;
   
   @NotBlank(message = "Please provide a group name")
   private String name;
   
   @ManyToOne
   private Budget budget;
   
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
}
