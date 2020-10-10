package pl.coderslab.warsztat2;

import java.sql.*;

public class DBUtil {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306?characterEncoding=UTF8&serverTimezone=UTC";
    private static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS workshop2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
    private static final String CREATE_TABLES = "CREATE TABLE IF NOT EXISTS workshop2.users (\n" +
                                                "id int unsigned auto_increment,\n" +
                                                "email varchar(255) unique,\n" +
                                                "username varchar(255),\n" +
                                                "password varchar(60),\n" +
                                                "primary key (id)\n" +
                                                ");";

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/workshop2?characterEncoding=UTF8&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "coderslab";

    public static Connection getConnection(boolean create) throws SQLException {
        if (create) {
            return DriverManager.getConnection(URL, DB_USER, DB_PASS);
        } else return null;
    }

    public static int createDB(Connection conn) throws SQLException  {
        try (PreparedStatement statement = conn.prepareStatement(CREATE_DATABASE)) {
            statement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static int createTables(Connection conn) throws SQLException  {
        try (PreparedStatement statement = conn.prepareStatement(CREATE_TABLES)) {
            statement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }



}
