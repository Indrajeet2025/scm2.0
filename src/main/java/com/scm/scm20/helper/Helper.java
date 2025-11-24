package com.scm.scm20.helper;

import com.scm.scm20.entities.Providers;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;

public class Helper
{
    public static String getEmailOfLoggedInUser(Authentication authentication)
    {
       // AuthenticatedPrincipal principal=(AuthenticatedPrincipal) authentication.getPrincipal();
        if(authentication instanceof OAuth2AuthenticationToken) {

            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();

            OAuth2User oAuth2User=(OAuth2User) authentication.getPrincipal();
            //String username="";
            // sign in with google
            if ("google".equalsIgnoreCase(registrationId)) {
                System.out.println("Getting email from google..");
                 String email = oAuth2User.getAttribute("email").toString();
//                name = oAuth2User.getAttribute("name");
//                picture = oAuth2User.getAttribute("picture");
//                provider = Providers.GOOGLE;
                return  email;
            }else {
              String email = oAuth2User.getAttribute("email");
                if (email == null || email.isBlank()) {
                    // Fallback: synthetic email from login (or you can force user to update later)
                    String login = oAuth2User.getAttribute("login");
                    email = login + "@github.local";   // or "@github.example"
                }
                System.out.println("Getting email from github..");
                return  email;

            }

        }
        else
        {
            System.out.println("Getting data from db..");
            return authentication.getName();
        }
    }
    public static  String getLinkForEmailVerification(String emailToken)
    {
        String link="http://localhost:8080/auth/verify-email?token="+emailToken;
        return  link;
    }
}
