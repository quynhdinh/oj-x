package org.ojx.repository;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.ojx.connection.ConnectionManager;
import org.ojx.model.User;

public class UserRepository {
    private static final String TABLE_NAME = "user";
    private static final Logger log = Logger.getLogger(UserRepository.class.getName());

    public Optional<User> getByUserName(String userName) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_name = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, userName);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(User.builder()
                                .userId(resultSet.getInt("user_id"))
                                .userType(resultSet.getString("user_type"))
                                .userName(resultSet.getString("user_name"))
                                .password(resultSet.getString("password"))
                                .email(resultSet.getString("email"))
                                .name(resultSet.getString("name"))
                                .country(resultSet.getString("country"))
                                .rating(resultSet.getInt("rating"))
                                .build());
                    }
                }
            }
        } catch (SQLException e) {
            log.warning("SQLException thrown:\n" + e.getMessage());
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM " + TABLE_NAME;
            try (var preparedStatement = conn.prepareStatement(sql);
                    var resultSet = preparedStatement.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    users.add(User.builder()
                            .userId(resultSet.getInt("user_id"))
                            .userType(resultSet.getString("user_type"))
                            .userName(resultSet.getString("user_name"))
                            .password(resultSet.getString("password"))
                            .email(resultSet.getString("email"))
                            .name(resultSet.getString("name"))
                            .country(resultSet.getString("country"))
                            .rating(resultSet.getInt("rating"))
                            .build());
                }
                return users;
            }
        } catch (SQLException e) {
            log.warning("SQLException thrown:\n" + e.getMessage());
        }
        return List.of();
    }

    public int createUser(User user) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO " + TABLE_NAME
                    + " (user_type, user_name, password, email, name, country, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getUserType());
                preparedStatement.setString(2, user.getUserName());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, user.getEmail());
                preparedStatement.setString(5, user.getName());
                preparedStatement.setString(6, user.getCountry());
                preparedStatement.setInt(7, user.getRating());
                return preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Optional<User> getById(int userId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_id = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setLong(1, userId);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(User.builder()
                                .userId(resultSet.getInt("user_id"))
                                .userType(resultSet.getString("user_type"))
                                .userName(resultSet.getString("user_name"))
                                .password(resultSet.getString("password"))
                                .email(resultSet.getString("email"))
                                .name(resultSet.getString("name"))
                                .country(resultSet.getString("country"))
                                .rating(resultSet.getInt("rating"))
                                .build());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public int isAuthenticated(String userName, String password) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT user_id, user_type FROM " + TABLE_NAME + " WHERE user_name = ? AND password = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, password);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String type = resultSet.getString("user_type");
                        return type.equals("admin") ? 2 : 1;
                    }
                }
            }
        } catch (SQLException e) {
            log.warning("SQLException thrown:\n" + e.getMessage());
            return -1;
        }
        return 0;
    }

    public int updateUser(User user) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "UPDATE " + TABLE_NAME
                    + " SET user_type = ?, user_name = ?, password = ?, email = ?, name = ?, country = ?, rating = ? WHERE user_id = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getUserType());
                preparedStatement.setString(2, user.getUserName());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, user.getEmail());
                preparedStatement.setString(5, user.getName());
                preparedStatement.setString(6, user.getCountry());
                preparedStatement.setInt(7, user.getRating());
                preparedStatement.setInt(8, user.getUserId());
                return preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            log.warning("SQLException thrown:\n" + e.getMessage());
            return -1;
        }
    }

    public int resetPassword(int userId, String newPassword) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "UPDATE " + TABLE_NAME + " SET password = ? WHERE user_id = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, newPassword);
                preparedStatement.setInt(2, userId);
                return preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            log.warning("SQLException thrown while resetting password:\n" + e.getMessage());
            return -1;
        }
    }

    public int deleteUser(int userId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE user_id = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                return preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            log.warning("SQLException thrown while deleting user:\n" + e.getMessage());
            return -1;
        }
    }
}