import domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserRepositoryORM implements UserRepositoryInterface {
    private final SessionFactory sessionFactory;

    public UserRepositoryORM(SessionFactoryORM sessionFactory) {
        this.sessionFactory = sessionFactory.getSessionFactory();
    }

    @Override
    public User getByUsername(String username) {
        List<User> userList = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction trans = null;
            try {
                trans = session.beginTransaction();
                userList = session.createQuery("FROM User WHERE username = :username", User.class).setParameter("username", username).list();
                trans.commit();
            }
            catch (Exception e1) {
                System.out.println("getByUsername(): Transaction failed");
                System.out.println("getByUsername(): " + e1.getMessage());
                if (trans != null) {
                    try {
                        trans.rollback();
                    }
                    catch (Exception e2) {
                        System.out.println("getByUsername(): Rollback failed");
                        System.out.println("getByUsername(): " + e2.getMessage());
                    }
                }
            }
        }
        assert userList != null;
        if (!userList.isEmpty()) {
            return userList.get(0);
        }
        return null;
    }
}
