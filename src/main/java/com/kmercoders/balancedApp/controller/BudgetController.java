package com.kmercoders.balancedApp.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.kmercoders.balancedApp.model.PaymentMethod;
import com.kmercoders.balancedApp.model.User;
import com.kmercoders.balancedApp.service.BudgetService;
import com.kmercoders.balancedApp.service.GroupService;
import com.kmercoders.balancedApp.service.PaymentMethodService;

@Controller
@RequestMapping(value = { "/budget"})
public class BudgetController {
   @Autowired
   private BudgetService budgetService;
   
   @Autowired
   private GroupService groupService;
   
   @Autowired
   private PaymentMethodService paymentMethodService;
  

   @GetMapping(value = { "list"})
   public String showBudgets(@AuthenticationPrincipal User user, ModelMap model) {
      TreeSet<Budget> budgets = budgetService.getBudgets(user);

      model.put("budgets", budgets);
      return "budget/list";
   }

   @GetMapping(value = "create")
   public String showBudgetCreationForm(ModelMap model) {
      Budget budget = new Budget();
      model.addAttribute("budget", budget);
      model.addAttribute("months", Month.values());
      model.addAttribute("currentMonth", LocalDate.now().getMonth());
      model.addAttribute("currentYear", LocalDate.now().getYear());
      return "budget/create";
   }

   @PostMapping(value = "create")
   public String createBudget(@AuthenticationPrincipal User user, ModelMap model, @ModelAttribute("budget") @Valid Budget budget, BindingResult result) {
      if (result.hasErrors()) {
         feedModel(model, budget);
         return "budget/create";
      }

      try {
         if(budgetService.getBudgets(user).isEmpty())
            addDefaultGroups(budget);
         else
            copyGroupsFromLastBudget(budget, user);
         
         budgetService.save(user, budget);
      } catch (Exception e) {
         e.printStackTrace();
         feedModel(model, budget);
         model.addAttribute("isDuplicate", true);
         return "budget/create";
      }
      return "redirect:/budget/list";
   }

   @GetMapping(value = "view/{budgetId}")
   public String showBudget(ModelMap model, @PathVariable Long budgetId) {
      Budget budget = budgetService.findById(budgetId).get();      
      model.put("budget", budget);
      
      return "budget/view";
   }
   
   @GetMapping(value = "transactions_by_payment_method/{budgetId}")
   public String transactionsByPaymentMethod(@AuthenticationPrincipal User user, ModelMap model, @PathVariable Long budgetId) {
      Budget budget = budgetService.findById(budgetId).get();
      TreeSet<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethods(user, budgetId);

      model.put("paymentMethods", paymentMethods);
      model.put("budget", budget);
      
      return "budget/transactionsByPaymentMethod";
   }

   @GetMapping(value = "edit/{budgetId}")
   public String showEditBudgetForm(ModelMap model, @PathVariable Long budgetId) {
      Budget budget = budgetService.findById(budgetId).get();

      feedModel(model, budget);

      return "budget/edit";
   }

   @PostMapping(value = "edit/{budgetId}")
   public String updateBudget(@AuthenticationPrincipal User user, ModelMap model, @ModelAttribute("budget") @Valid Budget budget, BindingResult result,
         @PathVariable Long budgetId) {
      Budget budgetFromDB = budgetService.findById(budgetId).get();

      if (result.hasErrors()) {
         budget.setId(budgetId);
         feedModel(model, budget);
         return "budget/edit";
      }

      budgetFromDB.setMonth(budget.getMonth());
      budgetFromDB.setYear(budget.getYear());

      try {
         budgetService.save(user, budgetFromDB);
      } catch (Exception e) {
         budget.setId(budgetId);
         feedModel(model, budget);
         model.addAttribute("isDuplicate", true);
         return "budget/edit";
      }
      return "redirect:/budget/list";
   }

   @RequestMapping("delete/{budgetId}")
   public String deleteBudget(@PathVariable Long budgetId) {
      budgetService.delete(budgetId);
      return "redirect:/budget/list";
   }

   public void feedModel(ModelMap model, Budget budget) {
      model.put("budget", budget);
      model.addAttribute("months", Month.values());
      model.addAttribute("currentMonth", budget.getMonth());
      model.addAttribute("currentYear", budget.getYear());
   }
   
   public void addDefaultGroups(Budget budget) {
      TreeSet<Group> defaultGroups = new TreeSet<>();
      
      Long maxGroupId = groupService.getMaxGroupId();
      Group income = new Group(++maxGroupId, "income");
      
      defaultGroups.add(income);
      income.setBudget(budget);
      budget.getGroups().add(income);
      
      Group savings = new Group(++maxGroupId, "savings");
      defaultGroups.add(savings);
      
      Group housing = new Group(++maxGroupId, "housing");
      defaultGroups.add(housing);
      
      Group transportation = new Group(++maxGroupId, "transportation");
      defaultGroups.add(transportation);
      
      Group food = new Group(++maxGroupId, "food");
      defaultGroups.add(food);
      
      Group health = new Group(++maxGroupId, "health");
      defaultGroups.add(health);
      
      Group insurance = new Group(++maxGroupId, "insurance");
      defaultGroups.add(insurance);
      
      Group debt = new Group(++maxGroupId, "debt");
      defaultGroups.add(debt);
      
      Group giving = new Group(++maxGroupId, "giving");
      defaultGroups.add(giving);
      
      Group miscellanious = new Group(++maxGroupId, "miscellanious");
      defaultGroups.add(miscellanious);
      
      for(Group grp: defaultGroups) {
         grp.setBudget(budget);
      }
      
      budget.getGroups().addAll(defaultGroups);
   }
   
   public void copyGroupsFromLastBudget(Budget budget, User user) {
      Budget lastBudget = budgetService.getLastBudget(user);
      Set<Group> lastBudgetGroups = lastBudget.getGroups();
      Long maxGroupId = groupService.getMaxGroupId();
      
      for(Group group: lastBudgetGroups) {
         Group newGroup = new Group(++maxGroupId, group.getName());
         Set<Category> currentGroupCategories = group.getCategories();
         for(Category category: currentGroupCategories) {
            Category newCategory = new Category(category.getName(), category.getAllocation());
            newCategory.setGroup(newGroup);
            newGroup.getCategories().add(newCategory);
         }
         newGroup.setBudget(budget);
         budget.getGroups().add(newGroup);
      }
   }
}
