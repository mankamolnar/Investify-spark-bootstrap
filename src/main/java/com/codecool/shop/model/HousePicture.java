package com.codecool.shop.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by flowerpower on 2017. 05. 07..
 */
public class HousePicture extends PsqlObject {

    private int houseId;
    private String url;
    private String description;

    public HousePicture() {
        this.id = 0;
    }

    public HousePicture(int id, int houseId, String url, String description) {
        this.id = id;
        this.houseId = houseId;
        this.url = url;
        this.description = description;
    }

    public void saveToPsql() {
        HousePicture.update(getId(), houseId, url, description);
    }

    // *** GETTERS & SETTERS
    public int getHouseId() {
        return houseId;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String toJson() {
        return "{\"id\":"+id+", \"houseId\":"+houseId+", \"url\":\""+url+"\", \"description\":\""+description+"\"}";
    }

    // *** STATIC METHODS (PSQL) ***
    public static ArrayList<HousePicture> findByHouse(Integer houseId) {
        String query = "SELECT * FROM housepictures WHERE houseid = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;
        ArrayList<HousePicture> result = new ArrayList<>();

        try {
            statement.setInt(1, houseId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(new HousePicture(
                        resultSet.getInt("id"),
                        resultSet.getInt("houseid"),
                        resultSet.getString("url"),
                        resultSet.getString("description")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    public static String findByHouseToJson(String id) {
        ArrayList<HousePicture> pictures = findByHouse(Integer.parseInt(id));
        String json = "[";

        for (HousePicture picture : pictures) {
            json += picture.toJson()+", ";
        }

        json = json.substring(0, json.length()-2)+"]";
        return json;
    }

    public static void insert(int houseId, String url, String description) {
        String query = "INSERT INTO housepictures (houseid, url, description) VALUES (?, ?, ?);";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, houseId);
            statement.setString(2, url);
            statement.setString(3, description);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static void update(int id, int houseId, String url, String description) {
        String query = "UPDATE housepictures SET houseId = ?, url = ?, description = ? WHERE id = ?";

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setInt(1, houseId);
            statement.setString(2, url);
            statement.setString(3, description);
            statement.setInt(4, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

}
