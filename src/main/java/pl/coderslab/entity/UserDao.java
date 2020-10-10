package pl.coderslab.entity;


import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.warsztat2.DBUtil;

import java.sql.*;
import java.util.Arrays;

public class UserDao {

    private static final String CREATE_USER_QUERY = "INSERT INTO users(username,email,password) VALUES (?, ?, ?)";
    private static final String READ_USER_QUERY = "SELECT id,username,email,password FROM users WHERE id=?;";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username=?,email=?,\n" +
                                                    "password=? WHERE id = ?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id=?;";
    private static final String COMPARE_PASSWORD = "SELECT password FROM users WHERE id=? and \n" +
                                                   "password!=?;";

    private static final String READ_USERS_QUERY = "SELECT id,username,email,password FROM users WHERE id IS NOT NULL;";
    private static final String READ_USERS_NAME_QUERY = "SELECT username,email,password FROM users WHERE name=?;";
    private static final String READ_USERS_NAME_LIKE_QUERY = "SELECT username,email,password FROM users WHERE name like='%?%';";
    private static final String READ_USERS_NAME_BEGIN_LIKE_QUERY = "SELECT username,email,password FROM users WHERE name like='?%';";
    private static final String READ_USERS_NAME_BEND_LIKE_QUERY = "SELECT username,email,password FROM users WHERE name like='%?';";
    private static final String READ_USERS_EMAIL_QUERY = "SELECT username,email,password FROM users WHERE email=?;";
    private static final String READ_USERS_EMAIL_LIKE_QUERY = "SELECT username,email,password FROM users WHERE email like='%?%';";
    private static final String READ_USERS_EMAIL_BEGIN_LIKE_QUERY = "SELECT username,email,password FROM users WHERE email like='?%';";
    private static final String READ_USERS_EMAIL_END_LIKE_QUERY = "SELECT username,email,password FROM users WHERE email like='?%';";

    private String setPassword(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(COMPARE_PASSWORD);
            statement.setLong(1, user.getId());
            statement.setString(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return user.getPassword();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hashPassword(user.getPassword());
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(long userId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            User user = new User();
            while (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int update(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, setPassword(user));
            statement.setLong(4, user.getId());
            statement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int delete(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
            statement.setLong(1, user.getId());
            statement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.
        return tmpUsers; // Zwracamy nową tablicę.
    }

    public User[] findAll() {
        User[] users = new User[0];
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_USERS_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users = addToArray(user,users);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
