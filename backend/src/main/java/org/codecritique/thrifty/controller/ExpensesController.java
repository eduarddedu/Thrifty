package org.codecritique.thrifty.controller;


import org.codecritique.thrifty.dao.ExpenseService;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.exception.WebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;


/**
 * @author Eduard Dedu
 */


@RestController
@RequestMapping("/rest-api/expenses")
public class ExpensesController extends BaseController {
    @Autowired
    private ExpenseService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createExpense(@RequestBody Expense expense) {
        try {
            service.store(expense);
            URI uri = toAbsoluteUri("/rest-api/expenses/" + expense.getId());
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            if (isConstraintViolationException(e))
                return ResponseEntity.badRequest().build();
            throw new WebException(e);
        }
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateExpense(@RequestBody Expense expense) {
        try {
            service.updateExpense(expense);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            if (isConstraintViolationException(e))
                return ResponseEntity.badRequest().build();
            throw new WebException(e);
        }
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Expense> getExpense(@PathVariable long id) {
        try {
            Expense expense = service.getExpense(id);
            if (expense == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(expense);
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeExpense(@PathVariable long id) {
        try {
            Expense expense = service.getExpense(id);
            if (expense == null)
                return ResponseEntity.notFound().build();
            service.removeExpense(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new WebException(e);
        }
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Expense> getExpensesSortedByDate() {
        try {
            return service.getExpenses();
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            path = "forPeriod")
    public ResponseEntity<Object> getExpensesForPeriod(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        try {
            List<Expense> expenses = service.getExpensesForPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate));
            return ResponseEntity.ok(expenses);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            path = "forYear")
    public ResponseEntity<Object> getExpensesForYear(@RequestParam("year") int year) {
        try {
            List<Expense> expenses = service.getExpensesForYear(year);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE, path = "sum")
    public String getExpensesTotalAmount() {
        try {
            return "" + service.getExpensesTotalAmount();
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

}