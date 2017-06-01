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
public class Sharehold extends PsqlObject {

    private int houseId;
    private int boughtPrice;
    private int soldPrice;
    private int monthlyIncome;
    private Date boughtDate;
    private Date soldDate;
    private int userId;
    private int sharehold;

    public Sharehold() {
        this.id = 0;
    }

    public Sharehold(int id, int houseId, int boughtPrice, int soldPrice, int monthlyIncome, Date boughtDate, Date soldDate, int userId, int sharehold) {
        this.id = id;
        this.houseId = houseId;
        this.boughtPrice = boughtPrice;
        this.soldPrice = soldPrice;
        this.monthlyIncome = monthlyIncome;
        this.boughtDate = boughtDate;
        this.soldDate = soldDate;
        this.userId = userId;
        this.sharehold = sharehold;
    }

    public void saveToPsql() {
        Sharehold.update(getId(), houseId, boughtPrice, soldPrice, monthlyIncome, boughtDate, soldDate, userId, sharehold);
    }

    // *** GETTERS & SETTERS
    public int getBoughtPrice() {
        return boughtPrice;
    }

    public int getSoldPrice() {
        return soldPrice;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public Date getBoughtDate() {
        return boughtDate;
    }

    public Date getSoldDate() {
        return soldDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSharehold() {
        return sharehold;
    }

    public void setSharehold(int sharehold) {
        this.sharehold = sharehold;
    }

    public House getHouse() {
        return House.find(houseId);
    }

    public int getHouseId() {
        return houseId;
    }

    public boolean isSellable() {
        if (Market.countShareholdsForSale(id) < sharehold) {
            return true;
        }
        return false;
    }

    public int getMaxSellable() {
        return sharehold-Market.countShareholdsForSale(id);
    }

    // *** STATIC METHODS (PSQL) ***
    public static Sharehold find(Integer id) {
        String query = "SELECT * FROM shareholds WHERE id = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;
        Sharehold result = new Sharehold();

        try {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();


            if (resultSet.next()) {
                result = new Sharehold(
                        resultSet.getInt("id"),
                        resultSet.getInt("houseId"),
                        resultSet.getInt("boughtPrice"),
                        resultSet.getInt("soldPrice"),
                        resultSet.getInt("monthlyIncome"),
                        resultSet.getDate("boughtDate"),
                        resultSet.getDate("soldDate"),
                        resultSet.getInt("userId"),
                        resultSet.getInt("sharehold")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    public static ArrayList findByUid(Integer uid) {
        String query = "SELECT * FROM shareholds WHERE userid = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;
        ArrayList result = new ArrayList();

        try {
            statement.setInt(1, uid);
            resultSet = statement.executeQuery();


            while (resultSet.next()) {
                result.add(new Sharehold(
                        resultSet.getInt("id"),
                        resultSet.getInt("houseId"),
                        resultSet.getInt("boughtPrice"),
                        resultSet.getInt("soldPrice"),
                        resultSet.getInt("monthlyIncome"),
                        resultSet.getDate("boughtDate"),
                        resultSet.getDate("soldDate"),
                        resultSet.getInt("userId"),
                        resultSet.getInt("sharehold")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    public static void insert(int houseId, int boughtPrice, int soldPrice, int monthlyIncome, Date boughtDate, Date soldDate, int userId, int sharehold) {
        String query = "INSERT INTO shareholds (houseId, boughtPrice, soldPrice, monthlyIncome, bougthDate, soldDate, userId, sharehold) VALUES (?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, houseId);
            statement.setInt(2, boughtPrice);
            statement.setInt(3, soldPrice);
            statement.setInt(4, monthlyIncome);
            statement.setDate(5, boughtDate);
            statement.setDate(6, soldDate);
            statement.setInt(7, userId);
            statement.setInt(8, sharehold);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static void update(int id, int houseId, int boughtPrice, int soldPrice, int monthlyIncome, Date boughtDate, Date soldDate, int userId, int sharehold) {
        String query = "UPDATE shareholds SET houseId = ?, boughtPrice = ?, soldPrice = ?, monthlyIncome = ?, boughtDate = ?, soldDate = ?, userId = ?, sharehold = ? WHERE id = ?";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, houseId);
            statement.setInt(2, boughtPrice);
            statement.setInt(3, soldPrice);
            statement.setInt(4, monthlyIncome);
            statement.setDate(5, boughtDate);
            statement.setDate(6, soldDate);
            statement.setInt(7, userId);
            statement.setInt(8, sharehold);
            statement.setInt(9, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

}
