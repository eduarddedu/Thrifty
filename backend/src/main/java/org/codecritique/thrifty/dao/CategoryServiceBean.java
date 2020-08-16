package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.exception.WebException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Eduard Dedu
 */

@Service
public class CategoryServiceBean extends BaseService implements CategoryService {

    @Override
    public void store(Category o) {
        try {
            super.persist(o);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    @Override
    public Category get(long id) {
        try {
            return (Category) super.find(Category.class, id);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    @Override
    public List<Category> getCategories() {
        return getCategoriesSortedByName();
    }

    private List<Category> getCategoriesSortedByName() {
        try {
            String sql = "Select r from Category r Order by r.name ";
            return em.createQuery(sql, Category.class).getResultList();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void update(Category category) {
        try {
            super.update(category);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void remove(long id) {
        try {
            super.remove(Category.class, id);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }
}
