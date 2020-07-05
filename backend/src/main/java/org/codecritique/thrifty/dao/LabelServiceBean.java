package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Service;

import java.util.List;

import org.codecritique.thrifty.entity.Label;

/**
 * @author Eduard Dedu
 */

@Service
public class LabelServiceBean extends BaseService implements LabelService {

    @Override
    public void store(Label label) {
        super.persist(label);
    }

    @Override
    public Label get(long id) {
        return em.find(Label.class, id);
    }

    @Override
    public List<Label> getLabels() {
       return getLabelsSortedByName();
    }

    @Override
    public void update(Label label) {
        Label label1 = em.find(Label.class, label.getId());
        if (label1 != null) {
            em.getTransaction().begin();
            label1.setName(label.getName());
            em.getTransaction().commit();
        }
    }

    @Override
    public void remove(long id) {
        Label label = em.find(Label.class, id);

        if (label == null)
            return;

        em.getTransaction().begin();
        for (Expense expense : label.getExpenses())
            expense.removeLabel(label);
        em.remove(label);
        em.getTransaction().commit();
    }

    private List<Label> getLabelsSortedByName() {
        String sql = "SELECT r from Label r ORDER BY r.name ";
        return em.createQuery(sql, Label.class).getResultList();
    }

}
