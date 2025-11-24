package com.scm.scm20.controllers;


import com.scm.scm20.entities.User;
import com.scm.scm20.helper.Helper;
import com.scm.scm20.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController
{
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication)
    {
        if(authentication==null)
        {
            return;
        }
        System.out.println("Adding info of loggein user...");
        String username= Helper.getEmailOfLoggedInUser(authentication);
        User user=userService.getUserByEmail(username);

        System.out.println(user);
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        logger.info("User logged in: {}",username);
        //fetch data from db to profile
        model.addAttribute("loggedInUser",user);
    }
}
