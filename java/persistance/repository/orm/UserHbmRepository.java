package persistance.repository.orm;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class UserHbmRepository extends HbmRepository<User, Integer>{

    @Override
    public User findById(Integer id) {
        User user = null;
        OrmUtils.initialize();
        try(Session session = OrmUtils.sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                user = session.find(User.class, id);
                tx.commit();

            } catch (RuntimeException ex) {
                System.err.println("Error at findById: "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        finally {
            OrmUtils.close();
        }
        return user;
    }

    @Override
    public Collection<User> getAll() {
        List<User> users = null;
        OrmUtils.initialize();
        try(Session session = OrmUtils.sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                users = session.createQuery("from User", User.class).list();
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Error at getAll: "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        finally {
            OrmUtils.close();
        }
        return users;
    }

    public User findOneByAlias(String alias) {
        User user = null;
        OrmUtils.initialize();
        try(Session session = OrmUtils.sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                user = session.createQuery("from User  where alias=:alias" , User.class).setParameter("alias", alias).setMaxResults(1) .uniqueResult();

                tx.commit();

            } catch (RuntimeException ex) {
                System.err.println("Error at findByAlias: "+ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        finally {
            OrmUtils.close();
        }
        return user;
    }
}
