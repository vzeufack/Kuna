package com.kmercoders.balancedApp.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kmercoders.balancedApp.model.Authority;
import com.kmercoders.balancedApp.model.User;
import com.kmercoders.balancedApp.repositories.UserRepository;

@Service
public class UserService {
    
   @Autowired
   private UserRepository userRepo;
    
   private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
   public User saveUser(User user) {
    Authority authority = new Authority();
    authority.setAuthority("ROLE_USER");
    authority.setUser(user);
    
    Set<Authority> authorities = new HashSet<>();
    authorities.add(authority);
    
    final String encryptedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encryptedPassword);    
    user.setAuthorities(authorities);
    user = userRepo.save(user); 
    
    return user;
   }
}
