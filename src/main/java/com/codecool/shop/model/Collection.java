package com.codecool.shop.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

/**
 * Created by flowerpower on 2017. 05. 07..
 */
public class Collection extends PsqlObject {

    private int payedIn;
    private int goal;
    private int shareholdPrice;
    private int investors;
    private Date started;
    private int active;

    public Collection() {
        this.id = 0;
    }

    public Collection(int id, int payedIn, int goal, int shareholdPrice, int investors, Date started, int active) {
        this.id = id;
        this.payedIn = payedIn;
        this.goal = goal;
        this.shareholdPrice = shareholdPrice;
        this.investors = investors;
        this.started = started;
        this.active = active;
    }

    public void payIn(int quantity) {
        investors += 1;
        payedIn += quantity * shareholdPrice;
        saveToPsql();
    }

    public void saveToPsql() {
        update(id, payedIn, goal, shareholdPrice, investors, active);
    }

    // *** STATIC METHODS (PSQL) ***
    public static Collection find(Integer id) {
        String query = "SELECT * FROM collections WHERE id = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        try {
            statement.setInt(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getInstanceFromQuery(statement);

    }

    public static Collection findActive() {
        String query = "SELECT * FROM collections WHERE active = 1;";
        PreparedStatement statement = getPreparedStatement(query);
        return getInstanceFromQuery(statement);

    }

    public static void insert(int payedIn, int goal, int shareholdPrice, int investors) {
        String query = "INSERT INTO collections (payedIn, goal, shareholdPrice, investors, started, active) VALUES (?, ?, ?, ?, ?, 1);";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, payedIn);
            statement.setInt(2, goal);
            statement.setInt(3, shareholdPrice);
            statement.setInt(4, investors);
            statement.setDate(5, new Date(System.currentTimeMillis()));
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static void update(int id, int payedIn, int goal, int shareholdPrice, int investors, int active) {
        String query = "UPDATE collections SET payedin = ?, goal = ?, shareholdprice = ?, investors = ?, active = ? WHERE collections.id = ?;";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, payedIn);
            statement.setInt(2, goal);
            statement.setInt(3, shareholdPrice);
            statement.setInt(4, investors);
            statement.setInt(5, active);
            statement.setInt(6, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    private static Collection getInstanceFromQuery(PreparedStatement statement) {
        try {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Collection(
                        Integer.parseInt(resultSet.getString("id")),
                        Integer.parseInt(resultSet.getString("payedIn")),
                        Integer.parseInt(resultSet.getString("goal")),
                        Integer.parseInt(resultSet.getString("shareholdPrice")),
                        Integer.parseInt(resultSet.getString("investors")),
                        resultSet.getDate("started"),
                        Integer.parseInt(resultSet.getString("active"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return new Collection(0, 0, 0, 0, 0, null, 0);
    }

    // *** GETTERS & SETTERS
    public int getPayedIn() {
        return payedIn;
    }

    public int getGoal() {
        return goal;
    }

    public int getShareholdPrice() {
        return shareholdPrice;
    }

    public Date getStarted() {
        return started;
    }

    public int getInvestors() {
        return investors;
    }

    public int getState() {
        return Math.round(Float.parseFloat(payedIn+"") / Float.parseFloat(goal+"") * 100);
    }
}
