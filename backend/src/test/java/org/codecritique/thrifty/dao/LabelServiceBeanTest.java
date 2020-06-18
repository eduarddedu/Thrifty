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
        Label label = getLabel("Cash");
        service.addLabel(label);
        assertEquals(label, service.getLabel(label.getId()));
    }

    private Label getLabel(String name) {
        Label label = new Label();
        label.setName(name);
        return label;
    }

    @Test
    void testGetLabelsSortedByName() {
        List<Label> list = new ArrayList<>();
        String [] arr = {"C", "A", "B"};
        for(int i = 0; i < 3; i++) {
            Label label = getLabel(arr[i]);
            list.add(label);
            service.addLabel(label);
        }
        assertEquals(list.get(0), service.getLabels().get(2));
    }

    @AfterAll
    void removeAll() {
        service.getLabels().forEach(label -> service.removeLabel(label.getId()));
    }

    @Test
    void testRemoveLabel() {
        Label label = getLabel("Rent");
        service.addLabel(label);
        service.removeLabel(label.getId());
        assertNull(service.getLabel(label.getId()));
    }

}