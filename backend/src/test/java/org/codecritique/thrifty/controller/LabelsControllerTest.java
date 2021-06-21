package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.codecritique.thrifty.Generator.stringSupplier;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class LabelsControllerTest extends BaseControllerTest {

    @Test
    void shouldCreateLabel() throws Exception {
        createLabel();
    }

    @Test
    void shouldReturnBadRequestWhenLabelNameIsEmptyString() throws Exception {
        Label label = new Label();
        label.setName("");
        mockMvc.perform(post(Resource.LABEL.url)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(mapper.writeValueAsString(label)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenLabelNameIsDuplicate() throws Exception {
        Label original = createLabel();
        Label duplicate = new Label();
        duplicate.setName(original.getName());
        mockMvc.perform(post(Resource.LABEL.url)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(mapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetLabels() throws Exception {
        mockMvc.perform(get(Resource.LABEL.url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void shouldUpdateLabel() throws Exception {
        Label label = createLabel();
        //exercise
        label.setName(stringSupplier.get());
        //verify
        assertEquals(label, updateAndGetEntity(label, Resource.LABEL));
    }

    @Test
    void shouldReflectLabelUpdateOnRelatedExpenses() throws Exception {
        //setup
        Expense expense = createExpense();
        assertTrue(expense.getLabels().isEmpty());
        Label label = createLabel();
        expense.addLabel(label);
        updateAndGetEntity(expense, Resource.EXPENSE);

        //exercise
        label.setName(stringSupplier.get());
        updateAndGetEntity(label, Resource.LABEL);

        //verify
        Expense sameExpense = (Expense) getEntity(Resource.EXPENSE, expense.getId());
        assertEquals(expense, sameExpense);
    }


    @Test
    void shouldRemoveLabel() throws Exception {
        Label label = createLabel();
        deleteEntity(Resource.LABEL, label.getId());
        mockMvc.perform(get(Resource.LABEL.url + label.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRemoveLabelLinkedToExpense() throws Exception {
        //setup
        Expense expense = createExpense();
        assertTrue(expense.getLabels().isEmpty());
        Label label = createLabel();
        expense.addLabel(label);
        updateAndGetEntity(expense, Resource.EXPENSE);

        //exercise
        deleteEntity(Resource.LABEL, label.getId());

        //verify
        mockMvc.perform(get(Resource.LABEL.url + label.getId()))
                .andExpect(status().isNotFound());
        Expense updated = (Expense) getEntity(Resource.EXPENSE, expense.getId());
        assertFalse(updated.getLabels().contains(label));
    }

}
