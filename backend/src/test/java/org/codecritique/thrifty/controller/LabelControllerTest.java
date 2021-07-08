package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.codecritique.thrifty.Generator.labelSupplier;
import static org.codecritique.thrifty.Generator.stringSupplier;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class LabelControllerTest extends BaseControllerTest {

    @Test
    void shouldCreateLabel() throws Exception {
        createAndGetLabel();
    }

    @Test
    void shouldReturnBadRequestOnCreateLabelWhenLabelNameIsEmptyString() throws Exception {
        Label label = new Label();
        label.setName("");
        label.setAccountId(1);
        mockMvc.perform(post(url(Label.class))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(mapper.writeValueAsString(label)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestOnCreateLabelWhenLabelNameIsDuplicate() throws Exception {
        Label original = createAndGetLabel();
        Label duplicate = new Label();
        duplicate.setName(original.getName());
        duplicate.setAccountId(1);
        mockMvc.perform(post(url(Label.class))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(mapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetLabels() throws Exception {
        mockMvc.perform(get(url(Label.class)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void shouldUpdateLabel() throws Exception {
        Label label = createAndGetLabel();
        //exercise
        label.setName(stringSupplier.get());
        //verify
        assertEquals(label, updateAndGetEntity(label));
    }

    @Test
    void shouldReflectLabelUpdateOnRelatedExpenses() throws Exception {
        //setup
        Expense expense = createAndGetExpense();
        assertTrue(expense.getLabels().isEmpty());
        Label label = createAndGetLabel();
        expense.addLabel(label);
        updateAndGetEntity(expense);

        //exercise
        label.setName(stringSupplier.get());
        updateAndGetEntity(label);

        //verify
        Expense sameExpense = getEntity(Expense.class, expense.getId());
        assertEquals(expense, sameExpense);
    }


    @Test
    void shouldRemoveLabel() throws Exception {
        Label label = createAndGetLabel();
        deleteEntity(Label.class, label.getId());
        mockMvc.perform(get(url(Label.class, label.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRemoveLabelLinkedToExpense() throws Exception {
        //setup
        Expense expense = createAndGetExpense();
        assertTrue(expense.getLabels().isEmpty());
        Label label = createAndGetLabel();
        expense.addLabel(label);
        updateAndGetEntity(expense);

        //exercise
        deleteEntity(Label.class, label.getId());

        //verify
        mockMvc.perform(get(url(Label.class, label.getId())))
                .andExpect(status().isNotFound());
        Expense updated = getEntity(Expense.class, expense.getId());
        assertFalse(updated.getLabels().contains(label));
    }

    @Test
    void shouldReturnForbiddenOnCreateLabelWhenLabelAccountIdDoesNotEqualUserAccountId() throws Exception {
        Label label = labelSupplier.get();
        label.setAccountId(2);
        String json = mapper.writeValueAsString(label);
        mockMvc.perform(post(url(Label.class)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "hacker@example.com")
    void shouldReturnForbiddenOnRemoveLabelWhenLabelAccountIdDoesNotEqualUserAccountId() throws Exception {
        mockMvc.perform(delete((url(Label.class, 1)))
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

}
