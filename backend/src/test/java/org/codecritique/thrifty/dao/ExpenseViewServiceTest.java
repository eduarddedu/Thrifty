package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.ExpenseView;
import org.codecritique.thrifty.entity.Label;
import org.codecritique.thrifty.entity.LabelView;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

public class ExpenseViewServiceTest extends BaseServiceBeanTest {

    @Autowired
    private ExpenseViewService service;

    @Test
    public void findById() {
        Expense expense = createAndStoreExpense();
        for (int i = 0; i < 2; i++) {
            expense.addLabel(createAndStoreLabel());
        }
        expenseService.updateExpense(expense);
        ExpenseView view = service.findById(expense.getId());
        assertNotNull(view);
        assertEquals(expense.getId(), view.getId());
        assertEquals(expense.getCreatedOn(), view.getCreatedOn());
        assertEquals(expense.getAmount(), view.getAmount());
        assertEquals(expense.getDescription(), view.getDescription());
        assertEquals(expense.getCategory().getId(), view.getCategory().getId());
        assertEquals(expense.getLabels().stream().map(Label::getId).collect(Collectors.toSet()),
                view.getLabels().stream().map(LabelView::getId).collect(Collectors.toSet()));

    }
}
