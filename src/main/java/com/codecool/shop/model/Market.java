package com.codecool.shop.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flowerpower on 2017. 05. 07..
 */
public class Market extends PsqlObject {

    private int shareholdId;
    private int sharehold;
    private int price;
    private Date started;
    private Date ended;
    private boolean active;

    public Market() {
        this.id = 0;
    }

    public Market(int id, int shareholdId, int sharehold, int price, Date started, Date ended, boolean active) {
        this.id = id;
        this.shareholdId = shareholdId;
        this.sharehold = sharehold;
        this.price = price;
        this.started = started;
        this.ended = ended;
        this.active = active;
    }

    public void saveToPsql() {
        Market.update(getId(), shareholdId, sharehold, price, started, ended, active);
    }

    public void buy(int userId) {
        Sharehold originalSharehold = Sharehold.find(shareholdId);
        User buyer = User.find(userId);

        if (originalSharehold.getSharehold() > sharehold) {
            System.out.println("ott");


            if (buyer.cashOut(price)) {
                buyer.saveToPsql();

                originalSharehold.setSharehold(originalSharehold.getSharehold()-sharehold);
                originalSharehold.saveToPsql();

                Sharehold.insert(
                    originalSharehold.getHouseId(),
                    originalSharehold.getBoughtPrice(),
                    originalSharehold.getSoldPrice(),
                    originalSharehold.getMonthlyIncome(),
                    originalSharehold.getBoughtDate(),
                    originalSharehold.getSoldDate(),
                    userId,
                    sharehold
                );
            }

        } else {
            System.out.println("itt");
            if (buyer.cashOut(price)) {
                System.out.println("ki tudta fizetni");
                originalSharehold.setUserId(userId);
                System.out.println(userId);
                originalSharehold.saveToPsql();
            }

        }
    }

    // *** GETTERS & SETTERS
    public int getSharehold() {
        return sharehold;
    }

    // *** STATIC METHODS (PSQL) ***
    public static Market find(String id) {
        String query = "SELECT * FROM market WHERE id = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;

        try {
            statement.setInt(1, Integer.parseInt(id));
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                new Market(
                        resultSet.getInt("id"),
                        resultSet.getInt("shareholdid"),
                        resultSet.getInt("sharehold"),
                        resultSet.getInt("price"),
                        resultSet.getDate("started"),
                        resultSet.getDate("ended"),
                        resultSet.getBoolean("active")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Market();
    }

    public static ArrayList<Sharehold> findAll() {
        String query = "SELECT shareholds.*, market.price FROM market LEFT JOIN shareholds ON market.sharehold = shareholds.id WHERE active = TRUE;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;
        ArrayList<Sharehold> result = new ArrayList();

        try {
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(new Sharehold(
                        resultSet.getInt("id"),
                        resultSet.getInt("houseId"),
                        resultSet.getInt("boughtPrice"),
                        resultSet.getInt("price"),
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

    public static List<Sharehold> findAllByUser(int userid) {
        String query = "SELECT shareholds.*, market.id as mid FROM market LEFT JOIN shareholds ON market.shareholdid = shareholds.id WHERE active = TRUE and shareholds.userid = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;
        ArrayList<Sharehold> result = new ArrayList();

        try {
            statement.setInt(1, userid);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(new Sharehold(
                        resultSet.getInt("mid"),
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

    public static void insert(int shareholdId, int sharehold, int price, Date started, Date ended, boolean active) {
        String query = "INSERT INTO market (shareholdId, sharehold, price, started, ended, active) VALUES (?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, shareholdId);
            statement.setInt(2, sharehold);
            statement.setInt(3, price);
            statement.setDate(4, started);
            statement.setDate(5, ended);
            statement.setBoolean(6, active);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static void update(int id, int shareholdId, int sharehold, int price, Date started, Date ended, boolean active) {
        String query = "UPDATE market SET shareholdId = ?, sharehold = ?, price = ?, started = ?, ended = ?, active = ? WHERE id = ?";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, shareholdId);
            statement.setInt(2, sharehold);
            statement.setInt(3, price);
            statement.setDate(4, started);
            statement.setDate(5, ended);
            statement.setBoolean(6, active);
            statement.setInt(7, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static int countShareholdsForSale(int shareholdId) {
        String query = "select count(id) as shareholds from market where shareholdid = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;

        try {
            statement.setInt(1, shareholdId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("shareholds");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return 0;
    }

    public static int countShareholdsForSaleByUser(int userId) {
        String query = "SELECT count(*) as shareholds FROM market LEFT JOIN shareholds ON shareholds.id = market.shareholdid WHERE userid = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;

        try {
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("shareholds");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return 0;
    }

    public static void delete(String id) {
        String query = "DELETE FROM market WHERE id = ?;";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, Integer.parseInt(id));
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
}
