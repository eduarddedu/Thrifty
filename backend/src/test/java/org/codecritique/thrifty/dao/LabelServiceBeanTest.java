package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes=org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
class LabelServiceBeanTest {

    @Autowired
    LabelServiceBean labelServiceBean;



    @Test
    void getLabels() {
        Label l = new Label();
        l.setName("Rent");
        labelServiceBean.addLabel(l);
        assertEquals(1, labelServiceBean.getLabels().size());
    }

    @Test
    void getLabelById() {
    }
}