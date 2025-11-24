package com.scm.scm20.controllers;

import com.scm.scm20.entities.User;
import com.scm.scm20.helper.Helper;
import com.scm.scm20.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController
{
    // user dashboard page


    @Autowired
    private UserService userService;

    private Logger logger= LoggerFactory.getLogger(UserController.class);

//    @ModelAttribute
//    public void addLoggedInUserInformation(Model model,Authentication authentication)
//    {
//        System.out.println("Adding info of loggein user...");
//        String username=Helper.getEmailOfLoggedInUser(authentication);
//        User user=userService.getUserByEmail(username);
//        System.out.println(user.getName());
//        System.out.println(user.getEmail());
//        logger.info("User logged in: {}",username);
//        //fetch data from db to profile
//        model.addAttribute("loggedInUser",user);
//    }

    @RequestMapping(value="/dashboard",method= RequestMethod.GET)
    public String userDashboard()
    {
        System.out.println("User Dashboard");
        return "user/dashboard";
    }

    // Blocking already loggedIn USer for acessing login form

    //User Profile Page
    @RequestMapping(value="/profile")
    public String userProfile(Model model, Authentication authentication)
    {
        return "user/profile";
    }
    // add contact page
    // user view contact page
    // user edit contact page
    // user delete contact page
    // user search contact page


}
