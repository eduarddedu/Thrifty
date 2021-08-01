package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.ExpenseView;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
interface ExpenseViewDao extends Repository<ExpenseView, Long> {
    ExpenseView findById(long id);
    List<ExpenseView> findByAccountId(long accountId);
}
