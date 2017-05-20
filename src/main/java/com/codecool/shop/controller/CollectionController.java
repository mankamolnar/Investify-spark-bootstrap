package com.codecool.shop.controller;

import com.codecool.shop.model.Collection;
import com.codecool.shop.model.DbConnection;
import com.codecool.shop.model.Investment;
import com.codecool.shop.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.LinkType.next;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class CollectionController {

    public enum pageName {
        COLLECTIONS
    }

    public static ModelAndView collections(Request req, Response res) {
        User user = new User();
        Collection collection = Collection.findActive();
        user.initFromSession(req.session());

        Map params = new HashMap<>();
        params.put("page", pageName.COLLECTIONS.name());
        params.put("collection", collection);
        params.put("user", user);

        if (user.getId() == 0) {
            return new ModelAndView(params, "index");
        } else {
            return new ModelAndView(params, "collections/index");
        }
    }

    public static ModelAndView payIn(Request req, Response res) {
        User user = new User();
        user.initFromSession(req.session());

        java.util.Date today = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, 6);
        today = calendar.getTime();

        Date now = new Date(System.currentTimeMillis());
        Date next = new Date(today.getTime());
        Collection collection = Collection.find(Integer.parseInt(req.params("cid")));

        Map params = new HashMap<>();
        params.put("error", "no");
        int quantity = Integer.parseInt(req.queryParams("quantity"));

        if (user.cashOut(quantity * collection.getShareholdPrice())) {
            collection.payIn(quantity);
            Investment.insert(quantity, collection.getShareholdPrice(), quantity * collection.getShareholdPrice(), now, next, false, user.getId());
            user.saveToSession(req.session());
            user.saveToPsql();
        } else {
            params.replace("error", "yes");
        }

        params.put("page", pageName.COLLECTIONS.name());
        params.put("collection", collection);
        params.put("user", user);

        if (user.getId() == 0) {
            return new ModelAndView(params, "index");
        } else {
            return new ModelAndView(params, "collections/index");
        }
    }

}
