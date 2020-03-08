package com.stl.crm.security;

import com.stl.crm.domain.User;
import com.stl.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
Мы должны сообщить Spring Security, где и как сохраняются пользователи и роли, чтобы он мог обращаться к этим данным сам и
обрабатывать аутентификацию. Чтобы выполнить аутентификацию, Spring Security необходимо сравнить представленные учетные
данные пользователя с пользователем приложения, хранящимся в базе данных.
Spring Security использует org.springframework.security.core.userdetails.UserDetailsService для извлечения информации о
пользователе из серверной базы данных. Нам нужно предоставить реализацию для интерфейса UserDetailsService.
 */
@Service
public class CrmUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /*
    Инфраструктура Spring Security ожидает, что метод loadUserByUsername вернет экземпляр типа UserDetails.
    Думайте о UserDetails как об адаптере между вашей собственной базой данных и тем, что нужно Spring Security в SecurityContextHolder.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("UserName " + username + " not found");
        }
        return new CrmUserDetails(user);
    }
}
