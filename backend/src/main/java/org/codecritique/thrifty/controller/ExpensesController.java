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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createExpense(@RequestBody Expense expense) {
        try {
            ExpenseServiceBean service = new ExpenseServiceBean();
            service.store(expense);
            return ResponseEntity.created(toAbsoluteUri("/rest-api/expenses/" + expense.getId())).build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Expense> getExpense(@PathVariable long id) {
        try {
            ExpenseServiceBean service = new ExpenseServiceBean();
            Expense expense = service.get(id);
            if (expense != null)
                return ResponseEntity.ok(expense);
            return ResponseEntity.notFound().build();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();
            throw ex;
        }
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateExpense(@RequestBody Expense expense) {
        try {
            ExpenseServiceBean service = new ExpenseServiceBean();
            service.update(expense);
            return ResponseEntity.ok().build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();
            throw ex;
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeExpense(@PathVariable long id) {
        try {
            ExpenseServiceBean service = new ExpenseServiceBean();
            Expense expense = service.get(id);
            if (expense != null) {
                service.remove(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Expense> getExpensesSortedByDate() {
        try {
            ExpenseServiceBean service = new ExpenseServiceBean();
            return service.getExpenses();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }

}