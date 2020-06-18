package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes=org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LabelServiceBeanTest {

    @Autowired
    LabelServiceBean service;

    @Test
    void testAddGetLabels() {
        Label label = new Label();
        label.setName("Cash");
        service.addEntity(label);
        assertEquals(label, service.getLabel(label.getId()));
    }

    @Test
    void testGetLabelsSortedByName() {
        List<Label> list = new ArrayList<>();
        String [] arr = {"A", "B", "C"};
        for(int i = 0; i < 3; i++) {
            Label o = new Label();
            o.setName(arr[i]);
            list.add(o);
            service.addEntity(o);
        }
        assertEquals(list.size(), service.getLabelsSortedByName().size());
        assertEquals(list, service.getLabelsSortedByName());
    }

    @AfterAll
    void removeAll() {
        service.getLabelsSortedByName().forEach(label -> service.removeLabel(label.getId()));
    }

    @Test
    void testRemoveLabel() {
        Label label = new Label();
        label.setName("Rent");
        service.addEntity(label);
        service.removeLabel(label.getId());
        assertNull(service.getLabel(label.getId()));
    }

}