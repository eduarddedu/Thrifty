package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public abstract class BaseService {

    @PersistenceContext
    protected EntityManager em;

    protected void update(BaseEntity entity) {
        BaseEntity oldEntity = em.find(entity.getClass(), entity.getId());
        if (oldEntity != null && !oldEntity.equals(entity))
            em.merge(entity);
    }

}
