package org.codecritique.thrifty.controller;


import org.codecritique.thrifty.dao.ExpenseServiceBean;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.exception.WebException;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;




/**
 * @author Eduard Dedu
 */


@RestController
@RequestMapping("/rest-api/expenses")
public class ExpensesController extends BaseController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createExpense(@RequestBody Expense expense) {
        try {
            ExpenseServiceBean service = new ExpenseServiceBean();
            service.store(expense);
            URI uri = toAbsoluteUri("/rest-api/expenses/" + expense.getId());
            return ResponseEntity.created(uri).build();
        } catch (Throwable th) {
            if (isConstraintViolationException(th))
                return ResponseEntity.badRequest().build();
            throw new WebException(th);
        }
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateExpense(@RequestBody Expense expense) {
        try {
            ExpenseServiceBean service = new ExpenseServiceBean();
            service.update(expense);
            return ResponseEntity.ok().build();
        } catch (Throwable th) {
            if (isConstraintViolationException(th))
                return ResponseEntity.badRequest().build();
            throw new WebException(th);
        }
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Expense> getExpense(@PathVariable long id) {
        try {
            ExpenseServiceBean service = new ExpenseServiceBean();
            Expense expense = service.get(id);
            if (expense == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(expense);
        } catch (Throwable th) {
            throw new WebException(th);
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeExpense(@PathVariable long id) {
        try {
            ExpenseServiceBean service = new ExpenseServiceBean();
            Expense expense = service.get(id);
            if (expense == null)
                return ResponseEntity.notFound().build();
            service.remove(id);
            return ResponseEntity.ok().build();
        } catch (Throwable th) {
            throw new WebException(th);
        }
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Expense> getExpensesSortedByDate() {
        try {
            ExpenseServiceBean service = new ExpenseServiceBean();
            return service.getExpenses();
        } catch (Throwable th) {
            throw new WebException(th);
        }
    }

}