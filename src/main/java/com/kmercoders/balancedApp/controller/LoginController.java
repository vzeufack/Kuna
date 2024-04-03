package com.kmercoders.balancedApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kmercoders.balancedApp.model.User;
import com.kmercoders.balancedApp.service.UserService;

@Controller
public class LoginController {
   @Autowired
   private UserService userService;
    
   @RequestMapping(value = {"/"}, method = {RequestMethod.GET})
   public String getLogin(ModelMap model) {
    User user = new User();
    model.put("user", user);
    
    return "login";
   }
   
   @RequestMapping(value = "/register", method = RequestMethod.GET)
   public String getRegister (ModelMap model) {
    User user = new User();
    model.put("user", user);
    
    return "register";
   }
   
   @RequestMapping(value = "/register", method = RequestMethod.POST)
   public String postRegister (@ModelAttribute User user, ModelMap model) {
	if(user.getUsername().isEmpty()) {
		model.put("error", "Please provide an email");    
        return "register";
	}
	
	if(user.getPassword().isEmpty()) {
		model.put("error", "Please provide a password");
        return "register";
	}
	
    if(!user.getPassword().equals(user.getConfirmPassword())) {
        model.put("error", "Passwords do not match! Please retype");    
        return "register";
    }
    
    try {
       user = userService.saveUser(user);
    }
    catch (Exception e) {
       model.put("error", "Username already exists");
       return "register";
    }
    
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
    
    return "redirect:/budget/list";
   }
}