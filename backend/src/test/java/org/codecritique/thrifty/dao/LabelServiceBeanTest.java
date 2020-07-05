package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

import static org.codecritique.thrifty.TestUtils.labelSupplier;
import static org.codecritique.thrifty.TestUtils.randomName;
import static org.junit.jupiter.api.Assertions.*;

class LabelServiceBeanTest extends BaseServiceBeanTest {

    @Autowired
    LabelServiceBean service;

    @Autowired
    protected LabelServiceBean labelService;

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

        Iterator<String> it = labelService.getLabels()
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

    @Test
    void removeLabel() {
        Label label = new Label("Foo");
        service.store(label);
        assertNotNull(service.get(label.getId()));
        service.remove(label.getId());
        assertNull(service.get(label.getId()));
    }

}