package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
@RequestMapping("/rest-api/expenses")
public class ExpensesController {

    @PersistenceContext
    EntityManager em;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Expense> getAllExpenses() {
        return em.createQuery("select e from Expense e order by e.date", Expense.class).getResultList();
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Expense getExpenseById(@PathVariable int id) {
       return em.find(Expense.class, id);
    }

}