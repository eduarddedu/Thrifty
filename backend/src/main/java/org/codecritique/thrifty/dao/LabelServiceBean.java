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
    public Label get(int id) {
        return em.find(Label.class, id);
    }

    @Override
    public List<Label> getLabelsSortedByName() {
        String sql = "SELECT r from Label r ORDER BY r.name ";
        return em.createQuery(sql, Label.class).getResultList();
    }

    @Override
    public void update(Label label) {
        if (em.find(Label.class, label.getId()) != null)
            super.persist(label);
    }

    @Override
    public void remove(int id) {
        Label label = em.find(Label.class, id);

        if (label == null)
            return;

        em.getTransaction().begin();
        for (Expense expense : label.getExpenses())
            expense.removeExpenseLabel(label);
        em.remove(label);
        em.getTransaction().commit();
    }

}
