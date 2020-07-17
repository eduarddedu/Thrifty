package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.ExpenseServiceBean;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.exception.WebException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Eduard Dedu
 */


@RestController
@RequestMapping("/rest-api/expenses")
public class ExpensesController extends BaseController {
    @Autowired
    ExpenseServiceBean service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createExpense(@RequestBody Expense expense) {
        try {
            service.store(expense);
            return ResponseEntity.created(toAbsoluteUri("/rest-api/expenses/" + expense.getId())).build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Expense> getExpense(@PathVariable long id) {
        try {
            Expense expense = service.get(id);
            if (expense != null)
                return ResponseEntity.ok(expense);
            return ResponseEntity.notFound().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateExpense(@RequestBody Expense expense) {
        try {
            service.update(expense);
            return ResponseEntity.ok().build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeExpense(@PathVariable long id) {
        try {
            Expense expense = service.get(id);
            if (expense != null) {
                service.remove(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Expense> getExpensesSortedByDate() {
        return service.getExpenses();
    }

}