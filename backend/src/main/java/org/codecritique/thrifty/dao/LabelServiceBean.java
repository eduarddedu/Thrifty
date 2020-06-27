package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LabelServiceBean extends BaseEntityService implements LabelService {

    @Override
    public void addLabel(Label label) {
        super.addEntity(label);
    }

    @Override
    public List<Label> getLabels() {
        String sql = "Select r from Label r Order by r.name ";
        return em.createQuery(sql, Label.class).getResultList();
    }

    @Override
    public Label getLabel(int id) {
        return em.find(Label.class, id);
    }

    @Override
    public void removeLabel(int id) {
        super.removeEntity(Label.class, id);
    }

    @Override
    public void updateLabel(Label label) {
        em.getTransaction().begin();
        em.find(Label.class, label.getId()).setName(label.getName());
        em.getTransaction().commit();
    }
}
