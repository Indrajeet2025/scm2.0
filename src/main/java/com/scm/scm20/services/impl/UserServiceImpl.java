package com.scm.scm20.services.impl;

import com.scm.scm20.entities.User;
import com.scm.scm20.helper.AppConstants;
import com.scm.scm20.helper.Helper;
import com.scm.scm20.helper.ResourceNotFoundException;
import com.scm.scm20.repositories.UserRepo;
import com.scm.scm20.services.EmailService;
import com.scm.scm20.services.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        //userId has to generate
       // String userId= UUID.randomUUID().toString();
        //password encoding
//        user.setPassword()
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // set the user roleList
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        logger.info(user.getProvider().toString());

       String emailToken= UUID.randomUUID().toString();
       user.setEmailToken(emailToken);
        User savedUser= userRepo.save(user);
       String emailLink= Helper.getLinkForEmailVerification(emailToken);
       emailService.sendEmail(savedUser.getEmail(),"Verify Email: smart Contact Manager",emailLink);
       return savedUser;

    }

    @Override
    public Optional<User> getUserById(String userId) {
        return userRepo.findById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional
    public User updateUser(String userId, Consumer<User> updater) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        updater.accept(u);    // 👈 here your lambda modifies 'u'
        return userRepo.save(u);
    }
//    public Optional<User> updateUser(User user) {
//
//        User user2=userRepo.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("User Not Found..."));
//        // update User2->user
//        user2.setName(user.getName());
//         user2.setEmail(user.getEmail());
//         user2.setPassword(user.getPassword());
    //      user2.setAbout(user.getAbout());
    //      user2.setPhoneNumber(user.getPhoneNumber());
    //       user2.setProfilePic(user.getProfilePic());
    //      user2.setEnabled(user.isEnabled());
    //      user2.setEmailVerified(user.isEmailVerified());
    //      user2.setPhoneVerified(user.isPhoneVerified());
    //      user2.setProvider(user.getProvider());
    //      user2.setProviderId(user.getProviderId());
    //      User save=userRepo.save(user2);
    //      return Optional.ofNullable(save);
//
//
//    }

    @Override
    public void deleteUser(String userId) {
      User u2=userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found....") );
      userRepo.delete(u2);
    }

    @Override
    public boolean isUserExist(String userId) {
        User u2=userRepo.findById(userId).orElseThrow(()->null);
        return u2!=null;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User u2=userRepo.findByEmail(email).orElseThrow(()->null);
        return u2!=null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
