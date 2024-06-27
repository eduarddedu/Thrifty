package org.codecritique.thrifty.repository;

import org.codecritique.thrifty.entity.ExpenseView;
import org.springframework.data.repository.Repository;

import java.util.List;


interface ExpenseViewRepository extends Repository<ExpenseView, Long> {
    ExpenseView findById(long id);

    List<ExpenseView> findByAccountId(long accountId);
}
