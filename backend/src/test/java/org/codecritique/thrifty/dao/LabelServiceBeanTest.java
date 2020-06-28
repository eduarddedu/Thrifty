package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class LabelServiceBeanTest extends BaseServiceBeanTest {

    @Autowired
    LabelServiceBean service;

    @Test
    void testAddLabel() {
        Label label = labelSupplier.get();
        service.store(label);
        assertEquals(label, service.get(label.getId()));
    }

    @Test
    void testGetLabelsSortedByName() {

        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            labelService.store(labelSupplier.get());

        Iterator<String> it = labelService.getLabelsSortedByName()
                .stream().map(Label::getName).iterator();

        while (it.hasNext()) {
            String name = it.next();
            if (it.hasNext()) {
                assertTrue(name.compareTo(it.next()) <= 0);
            }
        }
    }

    @Test
    void testUpdateLabel() {
        Label label = labelSupplier.get();
        service.store(label);

        label.setName(randomName.get());
        service.update(label);

        assertEquals(label, service.get(label.getId()));
    }

}