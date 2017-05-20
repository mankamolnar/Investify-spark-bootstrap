package com.codecool.shop.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by flowerpower on 2017. 05. 07..
 */
public class House extends PsqlObject {

    private int area;
    private int zipCode;
    private String city;
    private String address;
    private int predictedIncome;

    public House() {
        this.id = 0;
    }

    public House(int id, int area, int zipCode, String city, String address, int predictedIncome) {
        this.id = id;
        this.area = area;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
        this.predictedIncome = predictedIncome;
    }

    public void saveToPsql() {
        House.update(getId(), area, zipCode, city, address, predictedIncome);
    }

    // *** GETTERS & SETTERS
    public int getArea() {
        return area;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public int getPredictedIncome() {
        return predictedIncome;
    }

    public ArrayList<HousePicture> getPictures() {
        return HousePicture.findByHouse(getId());
    }

    // *** STATIC METHODS (PSQL) ***
    public static House find(Integer id) {
        String query = "SELECT * FROM houses WHERE houses.id = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;
        House result = new House();

        try {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = new House(
                        resultSet.getInt("id"),
                        resultSet.getInt("area"),
                        resultSet.getInt("zipCode"),
                        resultSet.getString("city"),
                        resultSet.getString("address"),
                        resultSet.getInt("predictedIncome")
                );
            }

        } catch (SQLException e) {

        }

        return result;

    }

    public static void insert(int area, int zipCode, String city, String address, int predictedIncome) {
        String query = "INSERT INTO houses (area, zipCode, city, address, bougthDate, predictedIncome) VALUES (?, ?, ?, ?, ?);";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, area);
            statement.setInt(2, zipCode);
            statement.setString(3, city);
            statement.setString(4, address);
            statement.setInt(5, predictedIncome);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static void update(int id, int area, int zipCode, String city, String address, int predictedIncome) {
        String query = "UPDATE houses SET area = ?, zipCode = ?, city = ?, address = ?, predictedIncome = ? WHERE id = ?";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, area);
            statement.setInt(2, zipCode);
            statement.setString(3, city);
            statement.setString(4, address);
            statement.setInt(5, predictedIncome);
            statement.setInt(6, id);
            System.out.println(statement);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

}
