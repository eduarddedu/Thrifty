package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.springframework.data.repository.Repository;

import java.util.List;

interface CategoryDao extends Repository<Category, Long> {
    List<Category> findByAccountId(long accountId);
}
