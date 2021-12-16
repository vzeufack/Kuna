package com.kmercoders.balancedApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmercoders.balancedApp.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
