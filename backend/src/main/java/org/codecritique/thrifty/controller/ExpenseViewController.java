package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.ExpenseView;
import org.codecritique.thrifty.entity.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest-api/view/expenses")
public class ExpenseViewController extends BaseController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ExpenseView> getExpenses(@AuthenticationPrincipal User user) {
        return repository.findExpenseViews(user.getAccountId());
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseView> getExpense(@PathVariable long id) {
        ExpenseView expense = repository.findById(ExpenseView.class, id);
        if (expense == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(expense);
    }
}
