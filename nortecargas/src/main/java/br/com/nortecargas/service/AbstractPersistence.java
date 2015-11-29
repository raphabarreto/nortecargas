package br.com.nortecargas.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.nortecargas.model.AbstractEntity;

public abstract class AbstractPersistence<T extends AbstractEntity, PK extends Number> {
    private Class<T> entityClass;

    public AbstractPersistence(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T save(T e) {
        if (e.getId() != null) {
            return (T)(this.getEntityManager().merge(e));
        }
        this.getEntityManager().persist(e);
        return e;
    }

    public void remove(T entity) {
        this.getEntityManager().remove(this.getEntityManager().merge(entity));
    }

    public T find(PK id) {
        return (T)(this.getEntityManager().find(this.entityClass, id));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> findAll() {
        CriteriaQuery cq = this.getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(this.entityClass));
        return this.getEntityManager().createQuery(cq).getResultList();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> findRange(int[] range) {
        CriteriaQuery cq = this.getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(this.entityClass));
        TypedQuery q = this.getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public int count() {
        CriteriaQuery cq = this.getEntityManager().getCriteriaBuilder().createQuery();
        Root rt = cq.from(this.entityClass);
        cq.select(this.getEntityManager().getCriteriaBuilder().count(rt));
        TypedQuery q = this.getEntityManager().createQuery(cq);
		return ((Long)q.getSingleResult()).intValue();
    }

    protected abstract EntityManager getEntityManager();
}
