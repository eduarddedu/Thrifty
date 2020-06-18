package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.ExpenseServiceBean;
import org.codecritique.thrifty.entity.Expense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest-api/expenses")
public class ExpensesController extends BaseController {
    @Autowired
    ExpenseServiceBean expenseServiceBean;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Expense> getExpenses() {
        return expenseServiceBean.getExpenses();
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Expense getExpenseById(@PathVariable int id) {
        return expenseServiceBean.getExpense(id);
    }

}