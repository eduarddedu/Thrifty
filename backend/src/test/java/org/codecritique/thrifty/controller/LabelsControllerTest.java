package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Label;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.codecritique.thrifty.TestUtils.labelSupplier;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class LabelsControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetLabels() throws Exception {
        createLabel();
        mockMvc.perform(get(LABEL_RESOURCE_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateLabel() throws Exception {
        Label label = createLabel();

        //exercise
        label.setName("Baz");
        mockMvc.perform(put(LABEL_RESOURCE_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(label)))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(LABEL_RESOURCE_PATH + "/" + label.getId())).andExpect(jsonPath("$.name").value("Baz"));
    }

    @Test
    void testDeleteLabel() throws Exception {
        Label label = createLabel();
        mockMvc.perform(delete(LABEL_RESOURCE_PATH + "/" + label.getId()))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(LABEL_RESOURCE_PATH + "/" + label.getId()))
                .andExpect(status().isNotFound());
    }
}
