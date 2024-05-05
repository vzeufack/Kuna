package com.kmercoders.balancedApp.controller;

import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kmercoders.balancedApp.model.Budget;
import com.kmercoders.balancedApp.model.Group;
import com.kmercoders.balancedApp.response.GroupResponse;
import com.kmercoders.balancedApp.service.BudgetService;
import com.kmercoders.balancedApp.service.GroupService;

@Controller
@RequestMapping(value = {"/budget/{budgetId}/group"})
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
   @ResponseBody
   public GroupResponse createGroup(@ModelAttribute @Valid Group group, BindingResult result, @PathVariable Long budgetId) {	   
	   GroupResponse response = new GroupResponse();
	   
       if (result.hasErrors()) {
    	  Map<String, String> errors = result.getFieldErrors().stream()
              .collect( Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

           response.setValidated(false);
           response.setErrorMessages(errors);
       } else {
           response.setValidated(true);
           group.setId(groupService.getMaxGroupId() + 1L);
           Budget budget = budgetService.findById(budgetId).get();
           group.setBudget(budget);
           groupService.save(group);
       }
       
       return response;
   }
   
   @PostMapping(value = "edit/{groupId}")
   @ResponseBody
   public GroupResponse updateGroup(@ModelAttribute @Valid Group group, BindingResult result, @PathVariable Long groupId, @PathVariable Long budgetId) {	   
	   GroupResponse response = new GroupResponse();
	   
       if (result.hasErrors()) {
    	  Map<String, String> errors = result.getFieldErrors().stream()
              .collect( Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

           response.setValidated(false);
           response.setErrorMessages(errors);
       } else {
           response.setValidated(true);
           Group groupFromDB = groupService.findById(groupId).get();
           groupFromDB.setName(group.getName());
           groupService.save(groupFromDB);
       }
       
       return response;
   }
   
   @RequestMapping("delete/{groupId}")
   public String deleteGroup(@PathVariable Long groupId, @PathVariable Long budgetId) {
      groupService.delete(groupId);
      return "redirect:/budget/view/" + budgetId;
   }
}
