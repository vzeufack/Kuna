package com.kmercoders.balancedApp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmercoders.balancedApp.model.Group;
import com.kmercoders.balancedApp.repositories.GroupRepository;

@Service
public class GroupService {
   @Autowired
   private GroupRepository groupRepo;
   
   public Group save(Group group) {
      return groupRepo.save(group);
   }
   
   public Optional<Group> findById(Long groupId) {
      return groupRepo.findById(groupId);
   }
   
   public void delete(Long groupId) {
      groupRepo.deleteById(groupId);
   }
}
