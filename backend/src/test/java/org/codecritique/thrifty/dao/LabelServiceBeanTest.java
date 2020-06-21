package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
        Label label = Label.getInstance("Cash");
        service.addLabel(label);
        assertEquals(label, service.getLabel(label.getId()));
    }


    @Test
    void testRemoveLabel() {
        Label label = Label.getInstance("Rent");
        service.addLabel(label);
        service.removeLabel(label.getId());
        assertNull(service.getLabel(label.getId()));
    }

}