package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public abstract class BaseDao<T extends BaseEntity> {

    @PersistenceContext
    protected EntityManager em;

    public void save(T entity) {
        em.persist(entity);
    }

    public void update(BaseEntity entity) {
        BaseEntity oldEntity = em.find(entity.getClass(), entity.getId());
        if (oldEntity != null && !oldEntity.equals(entity))
            em.merge(entity);
    }

    public T findById(Class<T> klass, long id) {
        return em.find(klass, id);
    }

    public void remove(Class<T> klass, long id) {
        T entity = em.find(klass, id);
        if (entity != null)
            em.remove(entity);
    }

}
