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
import com.kmercoders.balancedApp.model.Category;
import com.kmercoders.balancedApp.model.Group;
import com.kmercoders.balancedApp.response.GroupResponse;
import com.kmercoders.balancedApp.service.BudgetService;
import com.kmercoders.balancedApp.service.CategoryService;
import com.kmercoders.balancedApp.service.GroupService;

@Controller
@RequestMapping(value = { "/budget/{budgetId}/group/{groupId}/category"})
public class CategoryController {
   @Autowired
   private CategoryService categoryService;
   
   @Autowired
   private GroupService groupService;
   
   @Autowired
   private BudgetService budgetService;
   
   @PostMapping(value = "create")
   @ResponseBody
   public GroupResponse createCategory(@ModelAttribute @Valid Category category, BindingResult result, @PathVariable Long budgetId, @PathVariable Long groupId) {	   
	   GroupResponse response = new GroupResponse();
	   
       if (result.hasErrors()) {
    	  Map<String, String> errors = result.getFieldErrors().stream()
              .collect( Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

           response.setValidated(false);
           response.setErrorMessages(errors);
       } else {
           response.setValidated(true);
           Group group = groupService.findById(groupId).get();
           category.setGroup(group);
           categoryService.save(category);
       }
       
       return response;
   }
   
   @GetMapping(value = "edit/{categoryId}")
   public String showEditCategoryForm(ModelMap model, @PathVariable Long categoryId, @PathVariable Long groupId, @PathVariable Long budgetId) {
      Category category = categoryService.findById(categoryId).get();
      Budget budget = budgetService.findById(budgetId).get();
      Group group = groupService.findById(groupId).get();
      
      model.addAttribute("category", category);
      model.addAttribute("budget", budget);
      model.addAttribute("group", group);
      return "category/edit";
   }
   
   @PostMapping(value = "edit/{categoryId}")
   @ResponseBody
   public GroupResponse updateCategory(@ModelAttribute @Valid Category category, BindingResult result, @PathVariable Long categoryId, @PathVariable Long groupId, @PathVariable Long budgetId) {	   
	   GroupResponse response = new GroupResponse();
	   
       if (result.hasErrors()) {
    	  Map<String, String> errors = result.getFieldErrors().stream()
              .collect( Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

           response.setValidated(false);
           response.setErrorMessages(errors);
       } else {
           response.setValidated(true);
           Category categoryFromDB = categoryService.findById(categoryId).get();
           categoryFromDB.setName(category.getName());
           categoryFromDB.setAllocation(category.getAllocation());
           categoryService.save(categoryFromDB);
       }
       
       return response;
   }
   
   @RequestMapping("delete/{categoryId}")
   public String deleteCategory(@PathVariable Long categoryId, @PathVariable Long budgetId) {
      categoryService.delete(categoryId);
      return "redirect:/budget/view/" + budgetId;
   }
}
