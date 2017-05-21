package com.codecool.shop.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by flowerpower on 2017. 05. 08..
 */
public class DbConnection {

    private static final String DATABASE = "jdbc:postgres://kmapvworznkjow:1253bdbf8729f7a97e1ad8fdc5737ea9e4470975d0bce780d202d2ceef344027@ec2-54-228-235-185.eu-west-1.compute.amazonaws.com:5432/d7qsdjeli61kss?useUnicode=true&characterEncoding=UTF-8";
    private static final String DB_USER = "kmapvworznkjow";
    private static final String DB_PASSWORD = "1253bdbf8729f7a97e1ad8fdc5737ea9e4470975d0bce780d202d2ceef344027";
    public static Connection CONNECTION;

    public static void dbConnect() {
        if (CONNECTION == null) {
            tryConnect();
        }
    }

    private static void tryConnect() {
        try {
            CONNECTION = DriverManager.getConnection(
                    DATABASE,
                    DB_USER,
                    DB_PASSWORD
            );
        } catch (SQLException e) {
            System.out.println("Couldn't connect to Postgres server!");

        }
    }

    public static PreparedStatement getPreparedStatement(String query) {
        dbConnect();
        PreparedStatement statement = null;

        try {
            statement = CONNECTION.prepareStatement(query);

        } catch (SQLException e) {
            System.out.println("Couldn't find user");

        } finally {
            return statement;

        }
    }
}
