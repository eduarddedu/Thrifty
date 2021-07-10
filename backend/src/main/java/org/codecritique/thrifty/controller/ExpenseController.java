package org.codecritique.thrifty.controller;


import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/rest-api/expenses")
public class ExpenseController extends BaseController {

    @PreAuthorize("hasAuthority(#expense.accountId)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createExpense(@RequestBody Expense expense) {
        repository.save(expense);
        URI location = toAbsoluteURI("/rest-api/expenses/" + expense.getId());
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasAuthority(#expense.accountId)")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateExpense(@RequestBody Expense expense) {
        repository.updateEntity(expense);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Expense> getExpense(@PathVariable long id, @AuthenticationPrincipal User user) {
        Expense expense = repository.findById(Expense.class, id);
        if (expense == null)
            return ResponseEntity.notFound().build();
        if (isAuthorizedToAccess(user, expense))
            return ResponseEntity.ok(expense);
        throw new AccessDeniedException("Access is denied");
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeExpense(@PathVariable long id, @AuthenticationPrincipal User user) {
        Expense expense = repository.findById(Expense.class, id);
        if (expense == null)
            return ResponseEntity.notFound().build();
        if (isAuthorizedToAccess(user, expense)) {
            repository.removeExpense(id);
            return ResponseEntity.ok().build();
        }
        throw new AccessDeniedException("Access is denied");
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Expense> getExpensesSortedByDate(@AuthenticationPrincipal User user) {
        return repository.findExpenses(user.getAccountId());
    }

}