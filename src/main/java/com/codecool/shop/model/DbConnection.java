package com.codecool.shop.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by flowerpower on 2017. 05. 08..
 */
public class DbConnection {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/investify_hu?useUnicode=true&characterEncoding=UTF-8";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";
    public static Connection CONNECTION;

    public static void dbConnect() {
        if (CONNECTION == null) {
            tryConnect();
        }
    }

    private static void tryConnect() {
        try {
            URI dbUri = new URI(System.getenv("DATABASE_URL"));

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();


            CONNECTION = DriverManager.getConnection(
                    dbUrl,
                    username,
                    password
            );

        } catch (SQLException e) {
            System.out.println("Couldn't connect to Postgres server!");

        } catch (URISyntaxException e) {
            System.out.println("Urisyntax error!");
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
