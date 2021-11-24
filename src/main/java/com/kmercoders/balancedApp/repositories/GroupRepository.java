package com.kmercoders.balancedApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmercoders.balancedApp.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
