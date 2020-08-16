package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.exception.WebException;
import org.springframework.stereotype.Service;

import java.util.List;

import org.codecritique.thrifty.entity.Label;

import javax.persistence.EntityManager;

/**
 * @author Eduard Dedu
 */

@Service
public class LabelServiceBean extends BaseService implements LabelService {

    @Override
    public void store(Label label) {
        try {
            super.persist(label);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    @Override
    public Label get(long id) {
        try {
            return (Label) super.find(Label.class, id);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    @Override
    public List<Label> getLabels() {
        return getLabelsSortedByName();
    }

    @Override
    public void update(Label label) {
        try {
            super.update(label);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void remove(long id) {
        try {
            super.remove(Label.class, id);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    private List<Label> getLabelsSortedByName() {
        try {
            EntityManager em = emf.createEntityManager();
            String sql = "SELECT r from Label r ORDER BY r.name ";
            List<Label> labels = em.createQuery(sql, Label.class).getResultList();
            em.close();
            return labels;
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

}
