package com.stl.crm.repository;

import com.stl.crm.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    /*
    Spring Data JPA предоставит реализацию во время выполнения, чтобы метод findByUsername извлекал пользователя на основе заданного имени пользователя.
     */
    User findByUsername(String username);
}
