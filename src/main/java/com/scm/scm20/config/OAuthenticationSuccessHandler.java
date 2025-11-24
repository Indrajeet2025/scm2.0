package com.scm.scm20.config;

import com.scm.scm20.entities.Providers;
import com.scm.scm20.entities.User;
import com.scm.scm20.helper.AppConstants;
import com.scm.scm20.repositories.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(OAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        logger.info("OAuthenticationSuccessHandler");

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId(); // "google" or "github"

        logger.info("OAuth2 Provider: {}", registrationId);

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        oAuth2User.getAttributes().forEach((key, value) ->
                logger.info("{} => {}", key, value)
        );

        // Common data
        String email = null;
        String name = null;
        String picture = null;
        Providers provider = null;
        String providerUserId = oAuth2User.getName();

        // --------- GOOGLE ----------
        if ("google".equalsIgnoreCase(registrationId)) {

            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
            picture = oAuth2User.getAttribute("picture");
            provider = Providers.GOOGLE;
        }
        // --------- GITHUB ----------
        else if ("github".equalsIgnoreCase(registrationId)) {

            // email might be null for GitHub
            email = oAuth2User.getAttribute("email");
            if (email == null || email.isBlank()) {
                // Fallback: synthetic email from login (or you can force user to update later)
                String login = oAuth2User.getAttribute("login");
                email = login + "@github.local";   // or "@github.example"
            }

            // GitHub usually has "login" and sometimes "name"
            name = oAuth2User.getAttribute("name");
            if (name == null || name.isBlank()) {
                name = oAuth2User.getAttribute("login");
            }

            picture = oAuth2User.getAttribute("avatar_url");
            provider = Providers.GITHUB;
        }
        // --------- UNKNOWN PROVIDER ----------
        else {
            logger.error("Unknown OAuth2 provider: {}", registrationId);
            new DefaultRedirectStrategy().sendRedirect(request, response, "/login?error");
            return;
        }

        // FINAL SAFETY CHECK: email must not be null
        if (email == null || email.isBlank()) {
            logger.error("OAuth2 login without email. Provider: {}, principal: {}", registrationId, providerUserId);
            new DefaultRedirectStrategy().sendRedirect(request, response, "/login?error=email_missing");
            return;
        }

        // Check if user already exists
        User user = userRepo.findByEmail(email).orElse(null);

        if (user == null) {
            // New user
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setProfilePic(picture);
            user.setProvider(provider);
            user.setProviderUserId(providerUserId);
            user.setRoleList(List.of(AppConstants.ROLE_USER));
            user.setEnabled(true);
            user.setEmailVerified(true);
            user.setPhoneVerified(false);
            user.setAbout("This account is created using " + provider.name().toLowerCase());

            // if you need some default password / userId, handle here
            // user.setUserId(UUID.randomUUID().toString());
            // user.setPassword("oauth2User");
            logger.info("Creating new user: {}", email);
        } else {
            // Existing user - you may update details if you want
            logger.info("Existing user logging in: {}", email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProvider(provider);
            user.setProviderUserId(providerUserId);
        }

        userRepo.save(user);
        logger.info("User saved/updated successfully: {}", user.getEmail());

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}
