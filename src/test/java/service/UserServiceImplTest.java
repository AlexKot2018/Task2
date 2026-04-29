package service;

import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Ivan", "ivan@mail.com", 25);
        testUser.setId(1L);
    }

    @Test
    @DisplayName("Успешное сохранение пользователя")
    void saveUser_Positive() {
        when(userDAO.saveUser(testUser)).thenReturn(true);

        boolean result = userService.saveUser(testUser);

        assertTrue(result, "Сервис должен возвращать true при успешном сохранении");
        verify(userDAO, times(1)).saveUser(testUser);
    }

    @Test
    @DisplayName("Поиск пользователя по существующему ID")
    void getUser_ExistingId() {
        when(userDAO.getUser(1L)).thenReturn(testUser);

        User found = userService.getUser(1L);

        assertNotNull(found);
        assertEquals("Ivan", found.getName());
        verify(userDAO).getUser(1L);
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void getAllUsers_ShouldReturnList() {
        when(userDAO.getAllUsers()).thenReturn(List.of(testUser));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty(), "Список не должен быть пустым");
        assertEquals(1, users.size());
        verify(userDAO).getAllUsers();
    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    void deleteUser_Positive() {
        when(userDAO.deleteUser(1L)).thenReturn(true);

        boolean deleted = userService.deleteUser(1L);

        assertTrue(deleted);
        verify(userDAO).deleteUser(1L);
    }
}