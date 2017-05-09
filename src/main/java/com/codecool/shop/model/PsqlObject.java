package com.codecool.shop.model;


import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PsqlObject extends DbConnection {

    protected int id;

    // *** GETTERS & SETTERS ***
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(this);
                if (value != null) {
                    sb.append(field.getName() + ":" + value + ",");
                }
            } catch (IllegalAccessException e) {

            }
        }
        return sb.toString();
    }

}
