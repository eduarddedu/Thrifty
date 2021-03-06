package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.ExpenseViewDao;
import org.codecritique.thrifty.entity.ExpenseView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest-api/view/expenses")
public class ExpenseViewsController {

    @Autowired
    private ExpenseViewDao dao;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ExpenseView> getExpenses() {
        return dao.findAll();
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseView> getExpense(@PathVariable long id) {
        ExpenseView expense = dao.findById(id);
        if (expense == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(expense);
    }
}
