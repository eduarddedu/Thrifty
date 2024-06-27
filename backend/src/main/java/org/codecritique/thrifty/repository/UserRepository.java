package org.codecritique.thrifty.repository;

import org.codecritique.thrifty.entity.User;
import org.springframework.data.repository.Repository;

interface UserRepository extends Repository<User, Long> {
    User findByUsernameIgnoreCase(String email);
}