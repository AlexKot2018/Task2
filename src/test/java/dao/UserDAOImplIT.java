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

        assertTrue(saved);
        assertNotNull(user.getId());

        User fromDb = userDAO.getUser(user.getId());
        assertNotNull(fromDb);
        assertEquals("Petr", fromDb.getName());
    }

    @Test
    @DisplayName("Возврат null при поиске несуществующего ID")
    void integration_GetNonExistingUser() {
        User fromDb = userDAO.getUser(999L);
        assertNull(fromDb);
    }

    @Test
    @DisplayName("Ошибка при сохранении дубликата Email")
    void integration_SaveDuplicateEmail() {
        String email = "unique@mail.com";
        userDAO.saveUser(new User("First", email, 20));

        boolean savedDuplicate = userDAO.saveUser(new User("Second", email, 30));
        assertFalse(savedDuplicate, "Сохранение дубликата email не должно быть успешным");
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void integration_GetAllUsers() {
        userDAO.saveUser(new User("User1", "u1@mail.com", 20));
        userDAO.saveUser(new User("User2", "u2@mail.com", 22));

        List<User> users = userDAO.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    void integration_UpdateUser() {
        User user = new User("OldName", "old@mail.com", 40);
        userDAO.saveUser(user);

        user.setName("NewName");
        boolean updated = userDAO.updateUser(user);

        assertTrue(updated);
        assertEquals("NewName", userDAO.getUser(user.getId()).getName());
    }

    @Test
    @DisplayName("Удаление пользователя по ID")
    void integration_DeleteUser() {
        User user = new User("To Delete", "del@mail.com", 18);
        userDAO.saveUser(user);
        Long id = user.getId();
        boolean deleted = userDAO.deleteUser(id);

        assertTrue(deleted);
        assertNull(userDAO.getUser(id));
    }
}
