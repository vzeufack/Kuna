package com.kmercoders.balancedApp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kmercoders.balancedApp.model.Budget;
import com.kmercoders.balancedApp.model.Group;
import com.kmercoders.balancedApp.service.BudgetService;
import com.kmercoders.balancedApp.service.GroupService;

@Controller
@RequestMapping(value = { "/budget/{budgetId}/group"})
public class GroupController {
   @Autowired
   private GroupService groupService;
   
   @Autowired
   private BudgetService budgetService;
   
   @GetMapping(value = "create")
   public String showGroupCreationForm(ModelMap model, @PathVariable Long budgetId) {
      Group group = new Group();
      Budget budget = budgetService.findById(budgetId).get();
      
      model.put("group", group);
      model.addAttribute("budget", budget);
      return "group/create";
   }
   
   @PostMapping(value = "create")
   public String createGroup(ModelMap model, @ModelAttribute("group") @Valid Group group, BindingResult result, @PathVariable Long budgetId) {
      Budget budget = budgetService.findById(budgetId).get();
      
      if (result.hasErrors()) {
         model.addAttribute("group", group);
         model.addAttribute("budget", budget);
         return "group/create";
      }
      
      group.setId(groupService.getMaxGroupId() + 1L);
      group.setBudget(budget);
      //budget.getGroups().add(group);
      groupService.save(group);
      return "redirect:/budget/view/" + budgetId;
   }
   
   @GetMapping(value = "edit/{groupId}")
   public String showEditGroupForm(ModelMap model, @PathVariable Long groupId, @PathVariable Long budgetId) {
      Group group = groupService.findById(groupId).get();
      Budget budget = budgetService.findById(budgetId).get();
      
      model.addAttribute("group", group);
      model.addAttribute("budget", budget);
      
      return "group/edit";
   }
   
   @PostMapping(value = "edit/{groupId}")
   public String updateGroup(ModelMap model, @ModelAttribute("group") @Valid Group group, BindingResult result,
         @PathVariable Long groupId, @PathVariable Long budgetId) {
      Group groupFromDB = groupService.findById(groupId).get();
      Budget budget = budgetService.findById(budgetId).get();

      if (result.hasErrors()) {
         group.setId(groupId);
         model.addAttribute("group", group);
         model.addAttribute("budget", budget);
         return "group/edit";
      }
      
      groupFromDB.setName(group.getName());
      groupService.save(groupFromDB);
      
      return "redirect:/budget/view/" + budgetId;
   }
   
   @RequestMapping("delete/{groupId}")
   public String deleteGroup(@PathVariable Long groupId, @PathVariable Long budgetId) {
      groupService.delete(groupId);
      return "redirect:/budget/view/" + budgetId;
   }
}
