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
    private LabelServiceBean labelService;

    @Test
    void testAddLabel() {
        Label label = labelSupplier.get();
        labelService.store(label);
        assertEquals(label, labelService.get(label.getId()));
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
        labelService.store(label);

        label.setName(randomName.get());
        labelService.update(label);

        assertEquals(label, labelService.get(label.getId()));
    }

    @Test
    void removeLabel() {
        Label label = new Label("Foo");
        labelService.store(label);
        assertNotNull(labelService.get(label.getId()));
        labelService.remove(label.getId());
        assertNull(labelService.get(label.getId()));
    }

}