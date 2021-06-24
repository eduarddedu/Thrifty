package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.User;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserDao extends Repository<User, Long> {

    User save(User user);
    User findByUsernameIgnoreCase(String email);
    List<User> findAll();
}
