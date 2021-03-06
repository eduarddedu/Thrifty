package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.ExpenseView;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpenseViewDao extends Repository<ExpenseView, Long> {

    List<ExpenseView> findAll();
    ExpenseView findById(Long id);
}
