package com.kmercoders.balancedApp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmercoders.balancedApp.model.Category;
import com.kmercoders.balancedApp.repositories.CategoryRepository;

@Service
public class CategoryService {
   @Autowired
   private CategoryRepository categoryRepo;
   
   public Category save(Category group) {
      return categoryRepo.save(group);
   }
   
   public Optional<Category> findById(Long categoryId) {
      return categoryRepo.findById(categoryId);
   }
   
   public void delete(Long categoryId) {
      categoryRepo.deleteById(categoryId);
   }
}
