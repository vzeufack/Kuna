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
import com.kmercoders.balancedApp.model.Category;
import com.kmercoders.balancedApp.model.Group;
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
   
   
   @GetMapping(value = "create")
   public String showCategoryCreationForm(ModelMap model, @PathVariable Long budgetId, @PathVariable Long groupId) {
      Category category = new Category();
      Budget budget = budgetService.findById(budgetId).get();
      Group group = groupService.findById(groupId).get();
      
      model.put("category", category);
      model.addAttribute("budget", budget);
      model.addAttribute("group", group);
      return "category/create";
   }
   
   @PostMapping(value = "create")
   public String createCategory(ModelMap model, @ModelAttribute("category") @Valid Category category, BindingResult result, @PathVariable Long budgetId, @PathVariable Long groupId) {
      Budget budget = budgetService.findById(budgetId).get();
      Group group = groupService.findById(groupId).get();
      
      if (result.hasErrors()) {
         model.addAttribute("category", category);
         model.addAttribute("budget", budget);
         model.addAttribute("group", group);
         return "category/create";
      }
      
      category.setGroup(group);
      categoryService.save(category);
      return "redirect:/budget/view/" + budgetId;
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
   public String updateCategory(ModelMap model, @ModelAttribute("category") @Valid Category category, BindingResult result,
         @PathVariable Long categoryId, @PathVariable Long groupId, @PathVariable Long budgetId) {
      Category categoryFromDB = categoryService.findById(categoryId).get();
      Budget budget = budgetService.findById(budgetId).get();
      Group group = groupService.findById(groupId).get();

      if (result.hasErrors()) {
         category.setId(categoryId);
         model.addAttribute("category", category);
         model.addAttribute("budget", budget);
         model.addAttribute("group", group);
         return "category/edit";
      }
      
      categoryFromDB.setName(category.getName());
      categoryFromDB.setAllocation(category.getAllocation());
      categoryService.save(categoryFromDB);
      
      return "redirect:/budget/view/" + budgetId;
   }
   
   @RequestMapping("delete/{categoryId}")
   public String deleteCategory(@PathVariable Long categoryId, @PathVariable Long budgetId) {
      categoryService.delete(categoryId);
      return "redirect:/budget/view/" + budgetId;
   }
}
