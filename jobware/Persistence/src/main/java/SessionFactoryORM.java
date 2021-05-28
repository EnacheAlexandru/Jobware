import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SessionFactoryORM {

    private SessionFactory sessionFactory;

    public SessionFactoryORM() {
        sessionFactory = getSessionFactory();
    }

    private SessionFactory initialize() {
        SessionFactory session = null;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            session = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return session;
    }

    public SessionFactory getSessionFactory() {
        try {
            if (sessionFactory == null) {
                sessionFactory = initialize();
            }
            else if (sessionFactory.isClosed()) {
                sessionFactory = initialize();
            }
        }
        catch (Exception e) {
            System.err.println("Exception " + e);
        }
        return sessionFactory;
    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
