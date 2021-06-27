package org.codecritique.thrifty.controller;


import org.codecritique.thrifty.dao.ExpenseDao;
import org.codecritique.thrifty.entity.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/rest-api/expenses")
public class ExpensesController extends BaseController {
    @Autowired
    private ExpenseDao dao;

    @PreAuthorize("hasAuthority(#expense.accountId)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createExpense(@RequestBody Expense expense) {
        dao.store(expense);
        URI location = toAbsoluteURI("/rest-api/expenses/" + expense.getId());
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasAuthority(#expense.accountId)")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateExpense(@RequestBody Expense expense) {
        dao.updateExpense(expense);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Expense> getExpense(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        Expense expense = dao.getExpense(id);
        if (expense == null)
            return ResponseEntity.notFound().build();
        if (hasAuthority(userDetails, expense))
            return ResponseEntity.ok(expense);
        throw new AccessDeniedException("Access is denied");
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeExpense(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        Expense expense = dao.getExpense(id);
        if (expense == null)
            return ResponseEntity.notFound().build();
        if (hasAuthority(userDetails, expense)) {
            dao.removeExpense(id);
            return ResponseEntity.ok().build();
        }
        throw new AccessDeniedException("Access is denied");
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Expense> getExpensesSortedByDate() {
        return dao.getExpenses();
    }

}