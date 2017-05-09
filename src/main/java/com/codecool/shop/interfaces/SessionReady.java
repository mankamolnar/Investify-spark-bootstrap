package com.codecool.shop.interfaces;

import spark.Session;

public interface SessionReady {
    void saveToSession(Session session);
    void initFromSession(Session session);
}