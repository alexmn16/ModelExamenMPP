package persistance.repository.orm;

import model.Game;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class GameHbmRepository extends HbmRepository<Game, Integer>{

    @Override
    public Game findById(Integer id) {
        Game game = null;
        OrmUtils.initialize();
        try(Session session = OrmUtils.sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                game = session.find(Game.class, id);
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
        return game;
    }

    @Override
    public Collection<Game> getAll() {
        List<Game> games = null;
        OrmUtils.initialize();
        try(Session session = OrmUtils.sessionFactory.openSession()){
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                games = session.createQuery("from Game", Game.class).list();
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
        return games;
    }
}
