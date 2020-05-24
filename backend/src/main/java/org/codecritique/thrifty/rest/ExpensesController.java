package org.codecritique.thrifty.rest;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpensesController extends BaseController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/expenses")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello";
    }
}