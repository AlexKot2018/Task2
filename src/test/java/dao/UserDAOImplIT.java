package dao;

import model.User;
import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import util.HibernateUtil;
import java.util.List;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDAOImplIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private UserDAO userDAO;

    @BeforeAll
    static void beforeAll() {
        Properties props = new Properties();
        props.put("hibernate.connection.url", postgres.getJdbcUrl());
        props.put("hibernate.connection.username", postgres.getUsername());
        props.put("hibernate.connection.password", postgres.getPassword());
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        HibernateUtil.setTestConfiguration(props);
    }

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("delete from User").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Сохранение и получение пользователя по ID")
    void integration_SaveAndGet_User() {
        User user = new User("Petr", "petr@mail.com", 30);

        boolean saved = userDAO.saveUser(user);

        assertTrue(saved, "Пользователь должен быть сохранен");
        assertNotNull(user.getId(), "ID должен быть сгенерирован");

        User fromDb = userDAO.getUser(user.getId());
        assertNotNull(fromDb);
        assertEquals("Petr", fromDb.getName());
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void integration_GetAllUsers() {
        userDAO.saveUser(new User("User1", "u1@mail.com", 20));
        userDAO.saveUser(new User("User2", "u2@mail.com", 22));

        List<User> users = userDAO.getAllUsers();

        assertEquals(2, users.size(), "В списке должно быть ровно 2 пользователя");
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    void integration_UpdateUser() {
        User user = new User("OldName", "old@mail.com", 40);
        userDAO.saveUser(user);

        user.setName("NewName");
        boolean updated = userDAO.updateUser(user);

        assertTrue(updated);
        User fromDb = userDAO.getUser(user.getId());
        assertEquals("NewName", fromDb.getName(), "Имя должно обновиться в БД");
    }

    @Test
    @DisplayName("Удаление пользователя по ID")
    void integration_DeleteUser() {
        User user = new User("To Delete", "del@mail.com", 18);
        userDAO.saveUser(user);
        Long id = user.getId();

        boolean deleted = userDAO.deleteUser(id);

        assertTrue(deleted);
        assertNull(userDAO.getUser(id), "Пользователь не должен существовать в БД");
    }
}