package org.codecritique.thrifty.rest;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest-api/expenses")
public class ExpensesController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping()
    public String getAllExpenses() {
        return "Hello";
    }
}