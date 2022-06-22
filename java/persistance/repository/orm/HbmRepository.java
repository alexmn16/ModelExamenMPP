package persistance.repository.orm;

import model.Entity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistance.repository.Repository;

import java.util.Collection;

public abstract class HbmRepository<E extends Entity<ID>, ID> implements Repository<E, ID> {


    @Override
    public void add(E elem) throws Exception {
        OrmUtils.initialize();
        try(Session session = OrmUtils.sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(elem);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Error at add: "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        finally {
            OrmUtils.close();
        }
    }

    @Override
    public void delete(E elem) {
        OrmUtils.initialize();
        try(Session session = OrmUtils.sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.delete(elem);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Error at delete: "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        finally {
            OrmUtils.close();
        }
    }

    @Override
    public void update(E elem, ID id) {
        OrmUtils.initialize();
        try(Session session = OrmUtils.sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.update(elem);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Error at update: "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        finally {
            OrmUtils.close();
        }
    }

    @Override
    public abstract E findById(ID id);

    @Override
    public abstract Collection<E> getAll();



}
