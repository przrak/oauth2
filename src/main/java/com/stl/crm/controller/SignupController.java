package com.stl.crm.controller;

import com.stl.crm.domain.User;
import com.stl.crm.domain.UserRole;
import com.stl.crm.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class SignupController {

    @Autowired
    private SignupService signupService;

    /**
     *
     * user signup
     * @param user
     * @return
     */
    /*
    Зарегистрированный пользователь теперь может добавлять, получать, обновлять и удалять клиентов.
    Давайте предложим требование безопасности, которое позволяет только пользователям с ролью «ADMIN» удалять Клиента.
    Как ограничить метод deleteCustomer из класса CustomerController для пользователя с ролью «USER»?
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signup(@RequestBody User user) throws Exception {
        user.setRoles(Collections.singletonList(new UserRole("USER")));
        User newUser = signupService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
