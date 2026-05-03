package dao;

import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HibernateUtil;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Override
    public boolean saveUser(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Ошибка при сохранении: {}", e.getMessage());
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public User getUser(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            logger.error("Ошибка поиска ID {}: ", id, e);
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            logger.error("Ошибка получения списка: ", e);
            return List.of();
        }
    }

    @Override
    public boolean updateUser(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Ошибка обновления: ", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            int affectedRows = session.createMutationQuery("delete from User where id = :id")
                                      .setParameter("id", id)
                                      .executeUpdate();
            transaction.commit();
            return affectedRows > 0;
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Ошибка удаления ID {}: ", id, e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
}