package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public abstract class BaseService {

    @PersistenceContext
    protected EntityManager em;

    protected void persist(BaseEntity o) {
        em.persist(o);
    }

    protected BaseEntity find(Class<? extends BaseEntity> entityClass, long id) {
        return em.find(entityClass, id);
    }

    protected void update(BaseEntity entity) {
        BaseEntity oldEntity = em.find(entity.getClass(), entity.getId());
        if (oldEntity != null && !oldEntity.equals(entity))
            em.merge(entity);
    }

    protected void remove(Class<? extends BaseEntity> entityClass, long id) {
        BaseEntity entity = em.find(entityClass, id);
        if (entity != null)
            em.remove(entity);
    }

}
