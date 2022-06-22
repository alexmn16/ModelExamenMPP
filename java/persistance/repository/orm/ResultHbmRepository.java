package persistance.repository.orm;

import model.Result;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ResultHbmRepository extends HbmRepository<Result, Integer>{

    @Override
    public Result findById(Integer id) {
        Result result = null;
        OrmUtils.initialize();
        try(Session session = OrmUtils.sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                result = session.find(Result.class, id);
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
        return result;
    }

    @Override
    public Collection<Result> getAll() {
        List<Result> results = null;
        OrmUtils.initialize();
        try(Session session = OrmUtils.sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                results = session.createQuery("from Result", Result.class).list();
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
        return results;
    }
}
