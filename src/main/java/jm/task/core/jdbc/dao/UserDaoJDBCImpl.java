package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DeleteOrNot;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection;

    public UserDaoJDBCImpl() {

        this.connection = Util.getConnection();

    }

    public void createUsersTable() {

        String sqlCreate = "CREATE TABLE IF NOT EXISTS users2 (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                " name VARCHAR(45) NOT NULL," +
                " lastname VARCHAR(45) NOT NULL," +
                " age TINYINT NOT NULL," +
                "  PRIMARY KEY (id));";


        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlCreate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {

        String sqlDrop = "DROP TABLE IF EXISTS users2";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlDrop);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void saveUser(String name, String lastName, byte age) {

        PreparedStatement preparedStatement = null;

        String sqlSaveUser = "INSERT INTO users2 (name, lastname, age) VALUES(?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(sqlSaveUser);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            System.out.println("Запрос не прошел");
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }


    public void removeUserById(long id) {

        PreparedStatement preparedStatement = null;

        String sqlRemove = "DELETE FROM users2 WHERE id=?";

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sqlRemove);

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                System.out.println("Запрос не прошел");
                connection.rollback();

            } catch (SQLException e2) {
                e2.printStackTrace();
            }

        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();
        String sqlGetAll = "SELECT id, name, lastname, age FROM users2";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlGetAll)) {
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

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlClean)) {

            preparedStatement.executeUpdate();

        } catch (
                SQLException e) {
            e.printStackTrace();
        }

    }
}
