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
    public List<Label> getLabels(long accountId) {
        return getLabelsSortedByName(accountId);
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

    private List<Label> getLabelsSortedByName(long accountId) {
        String sql = "SELECT l from Label l where l.accountId = :accountId ORDER BY l.name";
        return em.createQuery(sql, Label.class).setParameter("accountId", accountId).getResultList();
    }

}
