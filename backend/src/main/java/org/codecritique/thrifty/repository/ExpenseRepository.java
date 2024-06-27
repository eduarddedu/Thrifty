package org.codecritique.thrifty.repository;

import org.codecritique.thrifty.entity.Expense;
import org.springframework.data.repository.Repository;

import java.util.List;


interface ExpenseRepository extends Repository<Expense, Long> {
    List<Expense> findByAccountId(long accountId);
}
