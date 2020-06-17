package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LabelServiceBean extends BaseServiceBean {

    public void addLabel(Label label) {
        em.getTransaction().begin();
        em.persist(label);
        em.getTransaction().commit();
    }

    public List<Label> getLabels() {
        String sql = "Select r from Label r Order by r.name";
        return em.createQuery(sql, Label.class).getResultList();
    }

    public Label getLabelById(int id) {
        return em.find(Label.class, id);
    }
}
