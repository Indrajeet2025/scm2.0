package com.scm.scm20.controllers;

import com.scm.scm20.entities.User;
import com.scm.scm20.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController
{

    @Autowired
    private UserRepo userRepo;

    //verify email
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token)
    {
       User user= userRepo.findByEmailToken(token).orElse(null);
       if(user!=null)
       {
           // user is fetched
           if(user.getEmailToken().equals(token))
           {
               user.setEmailVerified(true);
               user.setEnabled(true);
               userRepo.save(user);
               System.out.println("Email verfied...");
               return "success_page";
           }
           return "error_page";
       }
        return "error_page";
    }
}
