package com.stl.crm.security;

import com.stl.crm.domain.User;
import com.stl.crm.domain.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
UserDetails - это интерфейс в Spring Security, который предоставляет основную информацию о пользователе.
Давайте обеспечим реализацию этого интерфейса. Создайте класс с именем CrmUserDetails class в пакете com.stl.crm.security
и добавьте в него следующий код.
 */
public class CrmUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private Collection<? extends GrantedAuthority> authorities;
    private String password;
    private String username;

    public CrmUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = this.translate(user.getRoles());
    }

    /*
    CrmUserDetails создает набор экземпляров GrantedAuthority, которые представляют роли, которые пользователь имеет в системе.
    В Spring Security GrantedAuthority отражает разрешения, предоставленные пользователю.
    SimpleGrantedAuthority - это реализация интерфейса GrantedAuthority, предоставляемого Spring Framework.
     */
    /*
    Мы назначаем роли в виде строк, таких как ADMIN, USER, GUEST и т.д.
    Spring Security определяет роли, когда строка имеет префикс «ROLE_». Мы обрабатываем их в методе перевода.
     */
    /*
    После того, как Spring Security успешно проверил представленные учетные данные пользователем приложения,
    сохраненным в базе данных, он создает объект аутентификации и затем помещает его в SecurityContextHolder.
     */
    private Collection<? extends GrantedAuthority> translate(List<UserRole> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : roles) {
            String name = role.getName().toUpperCase();
            if (!name.startsWith("ROLE_")) {
                name = "ROLE_" + name;
            }
            authorities.add(new SimpleGrantedAuthority(name));
        }
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
