package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Expense;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest-api/expenses")
public class ExpensesController extends BaseController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Expense> getAllExpenses() {
        return em.createQuery("select e from Expense e order by e.createdOn", Expense.class).getResultList();
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Expense getExpenseById(@PathVariable int id) {
       return em.find(Expense.class, id);
    }

}