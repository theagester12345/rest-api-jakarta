package com.example.tech11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2DBUTIL {
    private static String JDBC_URL = "jdbc:h2:mem:tech11;DB_CLOSE_DELAY=-1";
//    private static String JDBC_USERNAME = "peter";
//    private static String JDBC_PASSWORD = "";
    private static final String JDBC_DRIVER = "org.h2.Driver";


    public static Connection getConnection() throws ClassNotFoundException {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("SQLite Database connected successfully");

            //Create User Table
            Statement statement = connection.createStatement();
            String create_user_sql = "create table if not exists users (id int primary key auto_increment ,firstname varchar(150),lastname varchar(150),email varchar(255), birthday date,passwordHash varchar(255));";
            statement.execute(create_user_sql);
            statement.close();


        } catch (SQLException  e) {
            printSQLException(e);
        }
        return connection;
    }

    public static void printSQLException(SQLException  ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
