package com.scm.scm20.config;

import com.scm.scm20.services.impl.SecurityCustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailsService securityCustomUserDetailsService;

    @Autowired
    private OAuthenticationSuccessHandler oAuthenticationSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // Normal username/password login
        httpSecurity.formLogin(form -> {
            form.loginPage("/login")
                    .loginProcessingUrl("/authenticate")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/user/profile", true)
                    .failureUrl("/login?error=true")
                    .failureHandler(authFailureHandler);

        });

        // ⭐ Google OAuth2 Login added here
//        httpSecurity.oauth2Login(oauth -> {
//            oauth.loginPage("/login"); // use same login page
//            oauth.defaultSuccessUrl("/user/dashboard", true);
//        });

        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login"); // use same login page
            oauth.successHandler(oAuthenticationSuccessHandler);
        });

        // Disable CSRF for easier testing
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Logout
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true);
        });

        // Use custom DAO provider
        httpSecurity.authenticationProvider(authenticationProvider());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
