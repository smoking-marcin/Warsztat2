package pl.coderslab.warsztat2;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;
import pl.coderslab.warsztat2.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class main {

    public static void main(String[] args) throws SQLException {
        /* Create database if doesn't exist */
        int result = 0;
        try (Connection conn = DBUtil.getConnection(true)) {
            result = DBUtil.createDB(conn);
            result += DBUtil.createTables(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result != 0) {
            System.out.println("T#is s#it is f@cked @p!");
        } else {
            System.out.println("This works!");

            UserDao userDao = new UserDao();

            User user = new User();
            user.setUserName("marcin");
            user.setEmail("marcin.mejl@gmail.pl");
            user.setPassword("klopsiki");

            User user1 = new User();
            user1.setUserName("ewa");
            user1.setEmail("ewa.mejl@gmail.com");
            user1.setPassword("kotleciki");

            userDao.create(user);
            userDao.create(user1);

            user.setUserName("Marcin");
            user1.setUserName("Ewa");

            userDao.update(user);
            userDao.update(user1);

            User[] users = new User[0];
            users = userDao.findAll();

            for (User u:users) {
                System.out.println(u.getId());
                System.out.println(u.getUserName());
                System.out.println(u.getEmail());
                System.out.println(u.getPassword());
            }

            user1 = userDao.read(user.getId());

            System.out.println(user.getId());
            System.out.println(user.getUserName());
            System.out.println(user.getEmail());
            System.out.println(user.getPassword());

            System.out.println(user1.getId());
            System.out.println(user1.getUserName());
            System.out.println(user1.getEmail());
            System.out.println(user1.getPassword());

            for (User u:users) {
                userDao.delete(u);
            }
        }
    }
}