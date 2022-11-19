package com.kmercoders.balancedApp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Authority implements GrantedAuthority{
   private static final long serialVersionUID = 1L;
   
   @Id
   @GeneratedValue
   private Long id;
   
   private String authority;
   
   @ManyToOne
   private User user;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getAuthority() {
      return authority;
   }

   public void setAuthority(String authority) {
      this.authority = authority;
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }
}
