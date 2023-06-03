package com.kmercoders.balancedApp.repositories;

import java.util.TreeSet;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmercoders.balancedApp.model.PaymentMethod;
import com.kmercoders.balancedApp.model.User;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>{
   TreeSet<PaymentMethod> findByUser(User user);
}
