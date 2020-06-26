package com.qingli.projectmanager.services;

import com.qingli.projectmanager.domian.User;
import com.qingli.projectmanager.exceptions.UsernameAlreadyExistException;
import com.qingli.projectmanager.exceptions.UsernameAlreadyExistResponse;
import com.qingli.projectmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());

            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new UsernameAlreadyExistException("Username '" + newUser.getUsername() + "' Already Exists");
        }
    }
}
