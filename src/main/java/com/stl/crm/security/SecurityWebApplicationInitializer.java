package com.stl.crm.security;

import com.stl.crm.config.AppConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/*
Зарегистрировать SpringSecurityFilterChain:
Spring Security предоставляет базовый класс AbstractSecurityWebApplicationInitializer, который гарантирует,
что springSecurityFilterChain будет зарегистрирован для каждого URL в нашем приложении.
Spring Security использует цепочку фильтров, которые будут перехватывать запрос, обнаруживать аутентификацию и
перенаправлять на точку входа аутентификации или передавать запрос в службу авторизации.
В конце концов, запрос либо попадет в класс Controller, либо выдаст исключение безопасности (не прошедшее проверку подлинности или неавторизованное).
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    public SecurityWebApplicationInitializer() {
        super(SecurityConfig.class, AppConfig.class);
    }
}
