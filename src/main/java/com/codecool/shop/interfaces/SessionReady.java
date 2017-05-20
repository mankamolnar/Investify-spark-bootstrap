package com.codecool.shop.interfaces;

import com.codecool.shop.model.User;
import spark.Session;

public interface SessionReady {
    void saveToSession(Session session);
    void initFromSession(Session session);

    static void destroyInSession(Session session, String param) {
        session.removeAttribute(param);
    }
}