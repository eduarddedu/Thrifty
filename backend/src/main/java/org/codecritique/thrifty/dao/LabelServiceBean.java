package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LabelServiceBean extends AbstractServiceBean {

    public List<Label> getLabels() {
        return em.createQuery("Select r from Label r Order by r.name", Label.class).getResultList();
    }

    public Label getLabelById(int id) {
        return em.find(Label.class, id);
    }
}
