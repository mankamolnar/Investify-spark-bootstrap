package com.codecool.shop.model;

import com.codecool.shop.interfaces.SessionReady;
import org.eclipse.jetty.websocket.common.events.ParamList;
import spark.Session;
import sun.security.provider.SHA;

import javax.jws.soap.SOAPBinding;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 07..
 */
public class User extends PsqlObject implements SessionReady {

    private String username;
    private String password;
    private String email;
    private int shareholds;
    private int cash;
    private int isActive;

    public User() {
        this.id = 0;
    }

    public User(int id, String username, String password, int shareholds, int cash, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.shareholds = shareholds;
        this.cash = cash;
        this.email = email;
    }

    // *** INSTANCE METHODS (SESSION) ***
    public void saveToSession(Session session) {
        session.attribute("user", this);
    }

    public void initFromSession(Session session) {
        if (session.attribute("user") != null) {
            User tmpUser = session.attribute("user");
            this.id = tmpUser.getId();
            this.username = tmpUser.getUsername();
            this.password = tmpUser.getPassword();
            this.shareholds = tmpUser.getShareholds();
            this.cash = tmpUser.getCash();
            this.email = tmpUser.getEmail();

        } else {
            this.id = 0;

        }
    }

    public void destroyInSession(Session session, String param) {
        SessionReady.destroyInSession(session, param);
    }

    public void saveToPsql() {
        User.update(getId(), username, password, email, cash, shareholds);
    }

    public void payIn(int money) {
        cash += money;
    }

    public boolean cashOut(int bill) {
        if (cash - bill >= 0) {
            cash = cash - bill;
            return true;
        }
        return false;
    }

    public void addSharehold(int quantity) {
        shareholds += quantity;
    }

    public int countInvestments() {
        return Investment.findByUid(getId()).size();
    }

    // *** GETTERS & SETTERS
    public ArrayList<Investment> getInvestments() {
        return Investment.findByUid(getId());
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getShareholds() {
        return Sharehold.findByUid(getId()).size();
    }

    public int getAllIncome() {
        ArrayList<Sharehold> shareholds = Sharehold.findByUid(getId());
        int income = 0;

        for (Sharehold sharehold : shareholds) {
            income += sharehold.getMonthlyIncome();
        }

        return income;
    }

    public int getCash() {
        return cash;
    }

    public String getEmail() {
        return email;
    }

    // *** STATIC METHODS (PSQL) ***
    public static User auth(String user, String password) {
        String query = "SELECT * FROM users WHERE username LIKE ? and password LIKE ?;";
        ResultSet resultSet;

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setString(1, user);
            statement.setString(2, password);
            statement.execute();
            resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return new User(
                        Integer.parseInt(resultSet.getString("id")),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        Integer.parseInt(resultSet.getString("shareholds")),
                        Integer.parseInt(resultSet.getString("cash")),
                        resultSet.getString("email")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return new User(0, "", "", 0, 0, "");
    }

    public static User find(Integer id) {
        String query = "SELECT * FROM users WHERE id = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;
        User result = new User(0,"","", 0, 0, "");

        try {
            statement.setString(1, id.toString());
            resultSet = statement.executeQuery();


            if (resultSet.next()) {
                result = new User(
                        Integer.parseInt(resultSet.getString("id")),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        Integer.parseInt(resultSet.getString("shareholds")),
                        Integer.parseInt(resultSet.getString("cash")),
                        resultSet.getString("email")
                );
            }

        } catch (SQLException e) {

        }

        return result;

    }

    public static void insert(String name, String password, String email, Integer cash, Integer shareholds) {
        String query = "INSERT INTO users (username, password, email, cash, shareholds) VALUES (?, ?, ?, ?, ?);";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setInt(4, cash);
            statement.setInt(5, shareholds);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static void update(Integer id, String name, String password, String email, Integer cash, Integer shareholds) {
        String query = "UPDATE users SET username = ?, password = ?, email = ?, cash = ?, shareholds = ? WHERE users.id = ?";
        System.out.println("ITTT : "+ email);
        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setInt(4, cash);
            statement.setInt(5, shareholds);
            statement.setInt(6, id);
            System.out.println(statement);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

}
