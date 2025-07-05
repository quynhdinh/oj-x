package org.ojx.service;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.ojx.model.User;

public interface UserService {
    Optional<User> getByUserName(String userName) throws SQLException;
    List<User> getAll();
    int save(User user);
    int isAuthenticated(String userName, String password) throws SQLException;
    // boolean isAdmin(String userName) throws SQLException;
    Optional<User> getById(int userId);
}