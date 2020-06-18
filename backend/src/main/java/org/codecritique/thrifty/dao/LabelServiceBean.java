package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LabelServiceBean extends BaseService implements LabelService {

    @Override
    public void addLabel(Label label) {
        em.getTransaction().begin();
        em.persist(label);
        em.getTransaction().commit();
    }

    @Override
    public List<Label> getLabelsOrderByName() {
        String s = "Select r from Label r Order by r.name";
        return em.createQuery(s, Label.class).getResultList();
    }

    @Override
    public Label getLabel(int id) {
        return em.find(Label.class, id);
    }

    @Override
    public void removeLabel(int id) {
        em.getTransaction().begin();
        em.remove(em.find(Label.class, id));
        em.getTransaction().commit();
    }
}
