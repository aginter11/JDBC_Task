package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DeleteOrNot;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        String sqlCreate = "CREATE TABLE IF NOT EXISTS users2 (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                " name VARCHAR(45) NOT NULL," +
                " lastname VARCHAR(45) NOT NULL," +
                " age TINYINT NOT NULL," +
                "  PRIMARY KEY (id));";


        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sqlCreate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {

        String sqlDrop = "DROP TABLE IF EXISTS users2";

        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sqlDrop);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void saveUser(String name, String lastName, byte age) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String sqlSaveUser = "INSERT INTO users2 (name, lastname, age) VALUES(?, ?, ?)";

        try {
            connection = Util.getConnection();
            preparedStatement = connection.prepareStatement(sqlSaveUser);
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(4, age);

            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            System.out.println("Запрос не прошел");
        }

    }


    public void removeUserById(long id) {

        String sqlRemove = "DELETE FROM users2 WHERE id=?";

        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlRemove)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();
        String sqlGetAll = "SELECT id, name, lastname, age FROM users2";

        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlGetAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));

                userList.add(user);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(userList);
        return userList;
    }

    public void cleanUsersTable() {

        String sqlClean = "DELETE FROM users2";

        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlClean)) {
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            boolean ok = DeleteOrNot.delete();
            if (ok) {
                connection.commit();
                System.out.println("Все пользователи удалены");
            } else {
                connection.rollback();

            }

        } catch (
                SQLException e) {
            e.printStackTrace();
        }

    }
}
