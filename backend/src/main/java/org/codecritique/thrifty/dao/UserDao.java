package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.User;
import org.springframework.data.repository.Repository;

interface UserDao extends Repository<User, Long> {
    User findByUsernameIgnoreCase(String email);
}