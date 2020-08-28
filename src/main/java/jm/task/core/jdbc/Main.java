package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;


import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {

        UserServiceImpl us = new UserServiceImpl();
//        us.createUsersTable();
        us.saveUser("Ivan", "Ivanov", (byte) 22);
        us.saveUser("Petr", "Petrov", (byte) 33);
        us.saveUser("Igor", "Sidorov", (byte) 55);
        us.cleanUsersTable();
    }
}
