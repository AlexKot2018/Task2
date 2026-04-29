package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public boolean saveUser(User user) {
        return userDAO.saveUser(user);
    }

    @Override
    public User getUser(Long id) {
        return userDAO.getUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        return userDAO.deleteUser(id);
    }
}
