package com.scm.scm20.controllers;

import com.scm.scm20.entities.User;
import com.scm.scm20.forms.UserForm;
import com.scm.scm20.helper.Message;
import com.scm.scm20.helper.MessageType;
import com.scm.scm20.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController
{

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index()
    {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model)
    {
        System.out.print("Home View");
        model.addAttribute("name","substring Technologies");
        model.addAttribute("youtubeChannel","Learn code with durgesh");
        model.addAttribute("gitRepo","https://github.com/Indrajeet2025");
        return "home";
    }

    // about page
    @RequestMapping("/about")
    public String about()
    {
        System.out.println("About view..");
        return  "about";
    }

    // services
    @RequestMapping("/services")
    public String services()
    {
        System.out.println("services view..");
        return  "services";
    }
    @RequestMapping("/contact")
    public String contact()
    {
        System.out.println("contact view..");
        return  "contact";
    }
    @RequestMapping("/login")
    public String login(Authentication authentication)
    {
        // If user is already logged in, redirect them
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            // Change this to wherever you want logged-in users to go
            return "redirect:/user/profile";   // or "redirect:/user/profile" or "redirect:/"
        }

        // If not logged in, show login page
        System.out.println("login view..");
        return  "login";
    }
    @RequestMapping("/register")
    public String register(Model model)
    {
        System.out.println("signup view..");
        UserForm userForm=new UserForm();
        model.addAttribute("userForm",userForm);
//        userForm.setUsername("Saurabh");
//        userForm.setPhonenumber("7666480851");
        return  "register";
    }

    // Processing register
    @RequestMapping(value = "/signup",method= RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session)
    {
        System.out.println("Processing registration....");
        //fetch the data
            //userForm class
        System.out.println(userForm.toString());
        //validate form data:todo
        if(bindingResult.hasErrors())
        {
            System.out.println("Validation errors: " + bindingResult);
            return "register";
        }
        // save to database
//           User user= User.builder()
//                   .name(userForm.getUsername())
//                   .email(userForm.getEmail())
//                   .password(userForm.getPassword())
//                   .about(userForm.getAbout())
//                   .phoneNumber(userForm.getPhoneNumber())
//                   .profilePic("/home/joshi/Java 2.0/Projects/scm2.0/src/main/resources/static/images/default profile.jpg")
//                   .build();
        User user=new User();
        user.setName(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("default.png");  // optional default pic
      /*
        user.setEnabled(true);
        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setProvider(Providers.SELF);  // assuming you have an enum for login provider
        user.setProviderUserId(null);
        */
        user.setEnabled(false);
        User savedUser=userService.saveUser(user);
          System.out.println("User Saved...");
        // add the message="Registration Successful"
        Message message =Message.builder().content("Registraion Successful..").type(MessageType.green).build();
        session.setAttribute("message",message);
        //redirect to register page
        return "redirect:/register";
    }
}
