package com.scm.scm20.services;

import com.scm.scm20.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService
{
    User saveUser(User user);
    Optional<User> getUserById(String userId);
    User getUserByEmail(String email);
    User updateUser(String userId, java.util.function.Consumer<User> updater);
    void deleteUser(String userId);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    List<User> getAllUsers();

}
