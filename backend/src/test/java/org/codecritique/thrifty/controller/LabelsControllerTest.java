package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.codecritique.thrifty.Generator.stringSupplier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class LabelsControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateLabel() throws Exception {
        createLabel();
    }

    @Test
    void testCreateLabelBadRequest() throws Exception {
        Label label = createLabel();
        Label clone = new Label(label.getName());
        String json = mapper.writeValueAsString(clone);
        mockMvc.perform(post(Resource.LABELS.url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetLabels() throws Exception {
        createLabel();
        mockMvc.perform(get(Resource.LABELS.url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateLabel() throws Exception {
        Label label = createLabel();
        //exercise
        label.setName(stringSupplier.get());
        //verify
        assertEquals(label, updateEntity(label, Resource.LABELS));
    }

    @Test
    void testUpdateLabelPropagatesToRelatedExpenses() throws Exception {
        //setup
        Expense expense = createExpense();
        assertTrue(expense.getLabels().isEmpty());
        Label label = createLabel();
        //link entities
        expense.addLabel(label);
        updateEntity(expense, Resource.EXPENSES);

        //exercise
        label.setName(stringSupplier.get());
        updateEntity(label, Resource.LABELS);

        //verify
        Expense sameExpense = (Expense) getEntity(Resource.EXPENSES, expense.getId());
        assertEquals(expense, sameExpense);
    }


    @Test
    void testRemoveLabel() throws Exception {
        Label label = createLabel();
        //exercise
        deleteEntity(Resource.LABELS, label.getId());
        //verify
        mockMvc.perform(get(Resource.LABELS.url + label.getId()))
                .andExpect(status().isNotFound());
    }
}
