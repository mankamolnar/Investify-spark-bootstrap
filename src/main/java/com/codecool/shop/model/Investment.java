package com.codecool.shop.model;

import com.codecool.shop.interfaces.SessionReady;
import spark.Session;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by flowerpower on 2017. 05. 07..
 */
public class Investment extends PsqlObject {

    private int uid;
    private int shareholds;
    private int price;
    private int priceAll;
    private Date startDate;
    private Date endDate;
    private boolean closed;

    public Investment() {
        this.id = 0;
    }

    public Investment(int id, int shareholds, int price, int priceAll, Date startDate, Date endDate, boolean closed, int uid) {
        this.id = id;
        this.uid = uid;
        this.shareholds = shareholds;
        this.price = price;
        this.priceAll = priceAll;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closed = closed;
    }

    public void setSharehold(int quantity) {
        shareholds = quantity;
    }

    public void saveToPsql() {
        Investment.update(getId(), shareholds, price, priceAll, startDate, endDate, closed, uid);
    }

    // *** STATIC METHODS (PSQL) ***
    public static Investment find(Integer id) {
        String query = "SELECT * FROM investments WHERE id = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;
        Investment result = new Investment();

        try {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = new Investment(
                        Integer.parseInt(resultSet.getString("id")),
                        Integer.parseInt(resultSet.getString("shareholds")),
                        Integer.parseInt(resultSet.getString("price")),
                        Integer.parseInt(resultSet.getString("priceAll")),
                        resultSet.getDate("startDate"),
                        resultSet.getDate("endDate"),
                        resultSet.getBoolean("closed"),
                        resultSet.getInt("uid")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    public static ArrayList<Investment> findByUid(Integer uid) {
        String query = "SELECT * FROM investments WHERE uid = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;
        ArrayList<Investment> result = new ArrayList<>();

        try {
            statement.setInt(1, uid);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(new Investment(
                        Integer.parseInt(resultSet.getString("id")),
                        Integer.parseInt(resultSet.getString("shareholds")),
                        Integer.parseInt(resultSet.getString("price")),
                        Integer.parseInt(resultSet.getString("priceAll")),
                        resultSet.getDate("startDate"),
                        resultSet.getDate("endDate"),
                        resultSet.getBoolean("closed"),
                        resultSet.getInt("uid")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    public static void insert(int shareholds, int price, int priceAll, Date startDate, Date endDate, boolean closed, int uid) {
        String query = "INSERT INTO investments (shareholds, price, priceAll, startDate, endDate, closed, uid) VALUES ( ?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, shareholds);
            statement.setInt(2, price);
            statement.setInt(3, priceAll);
            statement.setDate(4, startDate);
            statement.setDate(5, endDate);
            statement.setBoolean(6, closed);
            statement.setInt(7, uid);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static void update(int id, int shareholds, int price, int priceAll, Date startDate, Date endDate, boolean closed, int uid) {
        String query = "UPDATE investments SET shareholds = ?, price = ?, priceAll = ?, startDate = ?, endDate = ?, closed = ?, uid = ? WHERE users.id = ?";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(2, shareholds);
            statement.setInt(3, price);
            statement.setInt(4, priceAll);
            statement.setDate(5, startDate);
            statement.setDate(6, endDate);
            statement.setBoolean(7, closed);
            statement.setInt(8, uid);
            statement.setInt(9, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    // *** GETTERS & SETTERS
    public int getShareholds() {
        return shareholds;
    }

    public int getPrice() {
        return price;
    }

    public int getPriceAll() {
        return priceAll;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean getClosed() {
        return closed;
    }

    public int getUid() {
        return uid;
    }
}
