package com.stl.crm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
/*
Аннотация @EnableAuthorizationServer используется для настройки и включения сервера авторизации OAuth 2.0 и
включает различные конечные точки, необходимые для взаимодействия с сервером авторизации.
 */
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private static String REALM = "CRM_REALM";
    private static final int ONE_DAY = 60 * 60 * 24;
    private static final int THIRTY_DAYS = 60 * 60 * 24 * 30;

    @Autowired
    private CrmUserDetailsService crmUserDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    /*
    В первом методе настройки мы регистрируем данные клиента(приложения), не юзера. Для крупномасштабной системы этот
    процесс регистрации клиента будет следовать хорошо зарекомендовавшему себя процессу утверждения
    перед добавлением клиента на сервер авторизации. Для простоты мы храним данные клиента в памяти.
    Конфигуратор, который определяет сервис сведений о клиенте. Детали клиента могут быть инициализированы, или вы можете просто обратиться к существующему хранилищу.
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
//        clients
//                .inMemory()
//                .withClient("crmClient1")
//                .secret("crmSuperSecret")
//                .authorizedGrantTypes("password", "refresh_token") //Типы грантов, которые разрешено использовать клиенту
//                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT") //Полномочия, которые предоставляются клиенту (обычные полномочия Spring Security)
//                .scopes("read", "write", "trust")
//                //.accessTokenValiditySeconds(ONE_DAY)
//                //Когда мы регистрируем клиента с указанными выше атрибутами, мы также указываем срок действия сгенерированного
//                // токена доступа до 300 секунд (5 минут) и срок действия сгенерированного токена обновления до 30 дней.
//                .accessTokenValiditySeconds(300)
//                .refreshTokenValiditySeconds(THIRTY_DAYS);
    }

    /*
    Во втором методе configure (AuthorizationServerEndpointsConfigurer endpoints) мы устанавливаем
    несколько свойств (хранилище токенов, утверждения пользователей и AuthenticationManager) конечных
    точек сервера авторизации.
    Определяет конечные точки авторизации и токена и сервисы токена.
     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints
//                .tokenStore(tokenStore)
//                .userApprovalHandler(userApprovalHandler)
//                .authenticationManager(authenticationManager);
//    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore)
                .userApprovalHandler(userApprovalHandler)
                .authenticationManager(authenticationManager)
                .userDetailsService(crmUserDetailsService);
    }

    /*
    В последнем методе настройки мы просто переопределяем область безопасности Сервера авторизации.
    Определяет ограничения безопасности на конечной точке токена.
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .realm(REALM);
    }
}
