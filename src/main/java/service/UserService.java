package service;

import model.User;
import java.util.List;

public interface UserService {
    boolean saveUser(User user);
    User getUser(Long id);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(Long id);
}