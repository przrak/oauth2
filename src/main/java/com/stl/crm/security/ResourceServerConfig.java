package com.stl.crm.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/*
Сервер ресурсов обслуживает ресурсы, которые защищены токеном OAuth2.
Spring OAuth2 предоставляет фильтр аутентификации Spring Security, который реализует эту защиту.
 */
@Configuration
@EnableResourceServer
/*
@EnableResourceServer - это удобная аннотация для серверов ресурсов OAuth2,
она включает фильтр Spring Security, который аутентифицирует запросы через входящий токен OAuth2.
 */
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /*
    Метод configure (HttpSecurity http) настраивает правила доступа и сопоставления запросов (путь) для защищенных ресурсов с
    использованием класса HttpSecurity. Мы защищаем пути URL / api / *, позволяя только аутентифицированным пользователям,
    имеющим роль USER или ADMIN, получать к нему доступ.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //-- define URL patterns to enable OAuth2 security
        http.
                anonymous().disable()
                .requestMatchers()
                .antMatchers("/api/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").access("hasRole('ADMIN') or hasRole('USER')")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}