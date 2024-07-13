package com.kmercoders.balancedApp.controller;


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
import com.kmercoders.balancedApp.model.PaymentMethod;
import com.kmercoders.balancedApp.model.PaymentType;
import com.kmercoders.balancedApp.model.Transaction;
import com.kmercoders.balancedApp.model.User;
import com.kmercoders.balancedApp.service.PaymentMethodService;
import com.kmercoders.balancedApp.service.TransactionService;

@Controller
@RequestMapping(value = { "/paymentMethod"})
public class PaymentMethodController {
   @Autowired
   private PaymentMethodService paymentMethodService;
   
   @Autowired
   private TransactionService transactionService;
  

   @GetMapping(value = {"list"})
   public String showPaymentMethods(@AuthenticationPrincipal User user, ModelMap model) {
      TreeSet<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethods(user);

      model.put("paymentMethods", paymentMethods);
      return "paymentMethod/list";
   }

   @GetMapping(value = "create")
   public String showPaymentMethodCreationForm(ModelMap model) {
      PaymentMethod paymentMethod = new PaymentMethod();
      model.addAttribute("paymentMethod", paymentMethod);
      model.addAttribute("paymentTypes", PaymentType.values());
      model.addAttribute("paymentType", "");
      return "paymentMethod/create";
   }

   @PostMapping(value = "create")
   public String createPaymentMethod(@AuthenticationPrincipal User user, ModelMap model, @ModelAttribute("paymentMethod") @Valid PaymentMethod paymentMethod, BindingResult result) {
      if (result.hasErrors()) {
         feedModel(model, paymentMethod);
         return "paymentMethod/create";
      }

      try {
         paymentMethodService.save(user, paymentMethod);
      } catch (Exception e) {
         e.printStackTrace();
         feedModel(model, paymentMethod);
         model.addAttribute("isDuplicate", true);
         return "paymentMethod/create";
      }
      return "redirect:/paymentMethod/list";
   }

   @GetMapping(value = "view/{paymentMethodId}")
   public String showPaymentMethod(ModelMap model, @PathVariable Long paymentMethodId) {
      PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId).get();      
      model.put("paymentMethod", paymentMethod);
      
      return "paymentMethod/view";
   }

   @GetMapping(value = "edit/{paymentMethodId}")
   public String showEditPaymentMethodForm(ModelMap model, @PathVariable Long paymentMethodId) {
      PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId).get();
      feedModel(model, paymentMethod);
      model.addAttribute("paymentTypes", PaymentType.values());
      return "paymentMethod/edit";
   }

   @PostMapping(value = "edit/{paymentMethodId}")
   public String updatePaymentMethod(@AuthenticationPrincipal User user, ModelMap model, @ModelAttribute("paymentMethod") @Valid PaymentMethod paymentMethod, BindingResult result,
         @PathVariable Long paymentMethodId) {
      PaymentMethod paymentMethodFromDB = paymentMethodService.findById(paymentMethodId).get();

      if (result.hasErrors()) {
         paymentMethod.setId(paymentMethodId);
         feedModel(model, paymentMethod);
         return "paymentMethod/edit";
      }

      paymentMethodFromDB.setName(paymentMethod.getName());
      paymentMethodFromDB.setPaymentType(paymentMethod.getPaymentType());

      try {
         paymentMethodService.save(user, paymentMethodFromDB);
      } catch (Exception e) {
         paymentMethod.setId(paymentMethodId);
         feedModel(model, paymentMethod);
         model.addAttribute("isDuplicate", true);
         return "paymentMethod/edit";
      }
      return "redirect:/paymentMethod/list";
   }

   @RequestMapping("delete/{paymentMethodId}")
   public String deletePaymentMethod(@PathVariable Long paymentMethodId) {
	  Set<Transaction> transactions = paymentMethodService.findById(paymentMethodId).get().getTransactions();
	  for(Transaction transaction: transactions) {
		  transaction.setPaymentMethod(null);
	  }
      paymentMethodService.delete(paymentMethodId);
      return "redirect:/paymentMethod/list";
   }
   
   public void feedModel(ModelMap model, PaymentMethod paymentMethod) {
      model.put("paymentMethod", paymentMethod);
   }
}
