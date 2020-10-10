package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author Eduard Dedu
 */

@Service
public class LabelServiceBean extends BaseService implements LabelService {

    @Override
    @Transactional
    public void store(Label label) {
        super.persist(label);
    }

    @Override
    @Transactional
    public Label get(long id) {
        return (Label) super.find(Label.class, id);
    }

    @Override
    @Transactional
    public List<Label> getLabels() {
        return getLabelsSortedByName();
    }

    @Override
    @Transactional
    public void update(Label label) {
        super.update(label);
    }

    @Override
    @Transactional
    public void remove(long id) {
        super.remove(Label.class, id);
    }

    @Transactional
    private List<Label> getLabelsSortedByName() {
        String sql = "SELECT r from Label r ORDER BY r.name ";
        return em.createQuery(sql, Label.class).getResultList();
    }

}
