package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelDaoBean extends BaseDao implements LabelDao {

    @Override
    public void store(Label label) {
        em.persist(label);
    }

    @Override
    public Label getLabel(long id) {
        return em.find(Label.class, id);
    }

    @Override
    public List<Label> getLabels() {
        return getLabelsSortedByName();
    }

    @Override
    public void updateLabel(Label label) {
        super.update(label);
    }

    @Override
    public void removeLabel(long id) {
        Label label = em.find(Label.class, id);
        if (label != null) {
            label.getExpenses().forEach(e -> e.removeLabel(label));
            em.remove(label);
        }
    }

    private List<Label> getLabelsSortedByName() {
        String sql = "SELECT r from Label r ORDER BY r.name ";
        return em.createQuery(sql, Label.class).getResultList();
    }

}
