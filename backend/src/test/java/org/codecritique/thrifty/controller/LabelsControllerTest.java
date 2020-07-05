package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Label;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.codecritique.thrifty.TestUtils.labelSupplier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class LabelsControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final String resourcePath = "/rest-api/labels";
    private final String locationHeaderPattern = "http://localhost" + resourcePath + "/(\\d+)";

    @Test
    Label storeLabel() throws Exception {
        Label label = labelSupplier.get();
        String json = mapper.writeValueAsString(label);
        ResultActions actions = mockMvc.perform(post(resourcePath).contentType(MediaType.APPLICATION_JSON).content(json));
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.matchesPattern(locationHeaderPattern)));

        String locationHeaderValue = actions.andReturn().getResponse().getHeader("Location");
        long labelId = parseEntityIdFromLocationHeader(locationHeaderValue, locationHeaderPattern);
        actions = mockMvc.perform(get(resourcePath + "/" + labelId))
                .andDo(print())
                .andExpect(jsonPath("$.name").value(label.getName()));
        return mapper.readValue(actions.andReturn().getResponse().getContentAsString(), Label.class);
    }

    @Test
    void getLabels() throws Exception {
        storeLabel();
        mockMvc.perform(get(resourcePath))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void updateLabel() throws Exception {
        String json = mapper.writeValueAsString(new Label("Foo"));
        ResultActions actions = mockMvc.perform(post(resourcePath)
                .contentType(MediaType.APPLICATION_JSON).content(json));
        long labelId = parseEntityIdFromLocationHeader(actions.andReturn().getResponse().getHeader("Location"),
                locationHeaderPattern);
        json = mockMvc.perform(get(resourcePath + "/" + labelId))
                .andReturn().getResponse().getContentAsString();
        Label label = mapper.readValue(json, Label.class);
        assertEquals(label.getName(), "Foo");
        //exercise
        label.setName("Baz");
        mockMvc.perform(put(resourcePath).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(label)))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(resourcePath + "/" + label.getId())).andExpect(jsonPath("$.name").value("Baz"));
    }

    @Test
    void deleteLabel() throws Exception {
        Label label = storeLabel();
        mockMvc.perform(delete(resourcePath + "/" + label.getId()))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(resourcePath + "/" + label.getId()))
                .andExpect(status().isNotFound());
    }

}
