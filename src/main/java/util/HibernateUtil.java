package util;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import model.User;
import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory = buildSessionFactory(null);

    private static SessionFactory buildSessionFactory(Properties settings) {
        try {
            Configuration configuration = new Configuration().addAnnotatedClass(User.class);
            if (settings != null) {
                configuration.setProperties(settings);
            }

            return configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Ошибка инициализации SessionFactory: " + e);
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setTestConfiguration(Properties settings) {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        sessionFactory = buildSessionFactory(settings);
    }
}