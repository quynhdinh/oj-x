package org.ojx.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.ojx.model.User;
import org.ojx.repository.UserRepository;
import org.ojx.service.UserService;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getByUserName(String userName) throws SQLException {
        return userRepository.getByUserName(userName);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAllUsers();
    }

    @Override
    public int save(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public Optional<User> getById(int userId) {
        return userRepository.getById(userId);
    }

    @Override
    public int isAuthenticated(String userName, String password) throws SQLException {
        return userRepository.isAuthenticated(userName, password);
    }

    @Override
    public int update(User user) {
        return userRepository.updateUser(user);
    }

    @Override
    public boolean deleteUser(int userId) {
        try {
            int result = userRepository.deleteUser(userId);
            return result > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean resetPassword(int userId, String newPassword) {
        try {
            int result = userRepository.resetPassword(userId, newPassword);
            return result > 0;
        } catch (Exception e) {
            return false;
        }
    }

}
