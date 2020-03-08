package com.stl.crm.service;

import com.stl.crm.domain.User;
import com.stl.crm.domain.UserRole;
import com.stl.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
@Transactional
public class SignupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User addUser(User user) throws Exception {
        User existedUser = userRepository.findByUsername(user.getUsername());
        if(existedUser != null) {
            throw new Exception("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    /**
     *
     * set up a default user with two roles USER and ADMIN
     *
     */
    @PostConstruct
    private void setupDefaultUser() {
        //-- just to make sure there is an ADMIN user exist in the database for testing purpose
        if (userRepository.count() == 0) {
            userRepository.save(new User("crmadmin",
                    passwordEncoder.encode("adminpass"),
                    Arrays.asList(new UserRole("USER"), new UserRole("ADMIN"))));
        }
    }
}
