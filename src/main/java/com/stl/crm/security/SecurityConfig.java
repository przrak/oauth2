package com.stl.crm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/*
Аннотация @EnableWebSecurity и WebSecurityConfigurerAdapter работают вместе для обеспечения безопасности нашего приложения.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientDetailsService clientDetailsService;

    /*
    Нам нужно определить свойство dataSource. Добавьте следующий код в начале класса SecurityConfig.
     */
    @Autowired
    private DataSource dataSource;

    /*
    Метод globalUserDetails устанавливает хранилище пользователей в памяти с двумя пользователями и их ролями.
     */
    /*
    Откройте класс SecurityConfig в пакете com.stl.crm.security.
    Закомментируйте метод globalUserDetails и добавьте код следующим образом. Обязательно импортируйте класс CrmUserDetailsService.
     */
//    @Autowired
//    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("crmadmin").password("crmpass").roles("ADMIN","USER")
//                .and()
//                .withUser("crmuser").password("pass123").roles("USER");
//    }

    @Autowired
    private CrmUserDetailsService crmUserDetailsService;

    /*
    Мы переопределяем метод configure, который принимает AuthenticationManagerBuilder в качестве параметра.
    В нашей реализации метода мы используем AuthenticationManagerBuilder для добавления экземпляра CrmUserDetailsService.
    Вот как мы сообщаем Spring Security, где и как получить пользователя приложения из базы данных.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(crmUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /*
    Помните, мы зашифровали пароли при сохранении учетных данных пользователя в базе данных с использованием алгоритма BCrypt.
    Мы предоставили экземпляр BCryptPasswordEncoder для AuthenticationManagerBuilder, поэтому Spring Security будет использовать
    кодировщик паролей для сравнения простой строки, предоставленной пользователем, с зашифрованным хешем, хранящимся в базе данных.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        return new InMemoryClientDetailsService();
    }

    /*
    Метод configure (HttpSecurity http) определяет, какие URL-пути должны быть защищены в нашем приложении.
    Мы используем antMatchers для предоставления выражений URI в стиле Ant.
     */
    @Override
    /*
    Аннотация @Order используется для указания приоритета выполнения. Наивысший приоритет запускается первым.
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/about").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/oauth/token").permitAll()
                //.antMatchers("/api/**").authenticated()
                //.antMatchers("/api/**").hasRole("USER")
                //.antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                    .realmName("CRM_REALM");
    }

    /*
    Пути URL, предоставляемые платформой:
     /oauth/authorize (конечная точка авторизации),
     /oauth/token (конечная точка токена),
     /oauth/verify_access (утверждение сообщений пользователя для предоставления здесь),
     /oauth/error (используется для отображения ошибок на сервере авторизации),
     /oauth/check_token (используется серверами ресурсов для декодирования токенов доступа),
     /oauth/token_key (предоставляет открытый ключ для проверки токена при использовании токенов JWT).
     */


    /*
    AuthenticationManager: проверяет подлинность запроса
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /*
    TokenStore: сохраняет токены OAuth2 в памяти
     */
//    @Bean
//    public TokenStore tokenStore() {
//        return new InMemoryTokenStore();
//    }
    //-- use the JdbcTokenStore to store tokens
    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /*
    TokenStoreUserApprovalHandler: запоминает решения об утверждении, консультируясь с существующими токенами
     */
    @Bean
    @Autowired
    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);
        return handler;
    }

    /*
    TokenApprovalStore: хранилище ApprovalStore, которое работает с существующим TokenStore.
     */
    @Bean
    @Autowired
    public ApprovalStore approvalStore(TokenStore tokenStore) {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore);
        return store;
    }

}
