package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

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
        return (Label) super.find(Label.class, id);
    }

    @Override
    public List<Label> getLabels() {
        return getLabelsSortedByName();
    }

    @Override
    public void update(Label label) {
        super.update(label);
    }

    @Override
    public void remove(long id) {
        super.remove(Label.class, id);
    }

    private List<Label> getLabelsSortedByName() {
        EntityManager em = emf.createEntityManager();
        String sql = "SELECT r from Label r ORDER BY r.name ";
        List<Label> labels = em.createQuery(sql, Label.class).getResultList();
        em.close();
        return labels;
    }

}
