package com.scm.scm20.services.impl;

import com.scm.scm20.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityCustomUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserRepo userRepo;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // apne user ko load karo
//       return userRepo.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Username Not Found with this email"));
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .map(user -> {
                    System.out.println("Loaded user: " + user.getEmail() + " | enabled=" + user.isEnabled());
                    return user;
                })
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found with this email"));
    }

}
